/* 
 * Copyright 2015 Charles Allen Schultz II.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.claflin.cyfinder.internal.tasks;

import static edu.claflin.cyfinder.internal.Global.getDesktopService;
import static edu.claflin.cyfinder.internal.Global.getNetworkManagerService;
import static edu.claflin.cyfinder.internal.Global.getRootNetworkService;
import edu.claflin.cyfinder.internal.logic.ConfigurationBundle;
import edu.claflin.cyfinder.internal.logic.CyEdgeAdapter;
import edu.claflin.cyfinder.internal.logic.CyNodeAdapter;
import edu.claflin.cyfinder.internal.ui.ErrorPanel;
import edu.claflin.finder.algo.Algorithm;
import edu.claflin.finder.io.graph.SimpleGraphIO;
import edu.claflin.finder.io.graph.sub.GraphWriter;
import edu.claflin.finder.logic.Edge;
import edu.claflin.finder.logic.Graph;
import edu.claflin.finder.logic.Node;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.swing.*;

import edu.claflin.finder.logic.cygrouper.Communicator;
import edu.claflin.finder.logic.cygrouper.CygrouperNode;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.task.AbstractNetworkTask;
import org.cytoscape.work.TaskMonitor;

/**
 * Represents a Subgraph Finder Task.  Integrates with the stand-alone library 
 * with the same name to find subgraphs and return them in Cytoscape Format.
 * 
 * @author Charles Allen Schultz II
 * @version 1.4 June 19, 2015
 */
public class SubgraphFinderTask extends AbstractNetworkTask 
        implements PropertyChangeListener {
    
    /**
     * The Configuration Bundle used to configure this task.
     */
    private final ConfigurationBundle config;
    /**
     * The TaskMonitor currently used in reporting.
     */
    private TaskMonitor taskMonitor;

    /**
     * Constructs the Task.
     * @param network the Network to analyze.
     * @param config the Configuration to use.
     */
    public SubgraphFinderTask(CyNetwork network, ConfigurationBundle config) {
        super(network);
        this.config = config;
    }

    @Override
    public void run(final TaskMonitor taskMonitor) {
        try {
        this.taskMonitor = taskMonitor;
        taskMonitor.setTitle("Subgraph Finder");
        
        // Read CyNetwork into a Subgraph Finder Network
        Graph graph = null;
        if (!cancelled)
            graph = convertCyNetwork(taskMonitor);
        
        // Search for Subgraphs Here
        ArrayList<Graph> subgraphs = null;
        if (!cancelled)
            subgraphs = findSubGraphs(taskMonitor, graph);
        
        // Save Found Subgraphs To Current Network, child network, file...
        if (!cancelled)
            saveSubGraphs(taskMonitor, subgraphs);
        
        } catch (Throwable error) {
            SwingUtilities.invokeLater(() -> {
                String message = "An error occurred during execution!";
                ErrorPanel errorPanel = new ErrorPanel(message, error);
                errorPanel.display(getDesktopService().getJFrame(), message);
            });
            cancel();
        }
    }
    /**
     * Converts a CyNetwork to a finder Graph
     * @param taskMonitor the TaskMonitor to use to report progress.
     * @return the Graph containing the new network.
     */
    private Graph convertCyNetwork(final TaskMonitor taskMonitor) {
        taskMonitor.setStatusMessage("Converting Network...");
        taskMonitor.setProgress(-0.1D);
        
        Graph returnGraph = new Graph(network.toString());
        network.getNodeList().stream().forEach(cynode -> {
            String name = network.getDefaultNodeTable()
                    .getRow(cynode.getSUID()).get("shared name", String.class);
            CyNodeAdapter node = new CyNodeAdapter(cynode, name);
            returnGraph.addNode(node);
        });
        
        network.getEdgeList().stream().forEach(cyedge -> {
            String nodeName1 = network.getDefaultNodeTable()
                    .getRow(cyedge.getSource().getSUID())
                    .get("shared name", String.class);
            CyNodeAdapter node1 = (CyNodeAdapter) returnGraph.getNodeList()
                    .stream().filter(anode -> anode.getIdentifier()
                            .equals(nodeName1)).toArray()[0];
            
            String nodeName2 = network.getDefaultNodeTable()
                    .getRow(cyedge.getTarget().getSUID())
                    .get("shared name", String.class);
            CyNodeAdapter node2 = (CyNodeAdapter) returnGraph.getNodeList()
                    .stream().filter(anode -> anode.getIdentifier()
                            .equals(nodeName2))
                    .toArray()[0];
            
            Object data = null;
            if (config.getOrderingColumn() != null) {
                data = network.getRow(cyedge).getRaw(config.getOrderingColumn()
                        .getName());
            }
            
            CyEdgeAdapter edge = new CyEdgeAdapter(node1, node2, data, cyedge);
            returnGraph.addEdge(edge);
            
            // Add second edge if undirected.
            if (!cyedge.isDirected()) {
                edge = new CyEdgeAdapter(node2, node1, data, cyedge);
                returnGraph.addEdge(edge);
            }
        });
        
        return returnGraph;
    }
    /**
     * Processes the graph and returns the subgraphs.
     * @param taskMonitor the TaskMonitor to use to report progress.
     * @param target the Graph object to analyze.
     * @return the ArrayList containing the subgraphs.
     */
    private ArrayList<Graph> findSubGraphs(final TaskMonitor taskMonitor, Graph target) {
        taskMonitor.setStatusMessage("Searching for subgraphs...");
        taskMonitor.setProgress(0D);

        Algorithm algo = config.getAlgo();
        algo.addPropertyChangeListener(this);
        ArrayList<Graph> graphs = algo.process(target);
        return graphs;
    }
    
    private void saveSubGraphs(final TaskMonitor taskMonitor, ArrayList<Graph> subgraphs) {
    	
        taskMonitor.setStatusMessage("Saving subgraphs...");
        taskMonitor.setProgress(0D);
        int operationCount = 0;
        if (config.isInPlace()) operationCount++;
        if (config.isNewChild()) operationCount++;
        if (config.isSaveToFile()) operationCount++;
        int completedOperations = 0;
        
        // Add inplace annotations.
        if (config.isInPlace()) {
            int count = 0;
            for (Graph graph : subgraphs) {
                String name;
                do {
                    name = "SG" + (++count);
                } while (network.getDefaultNodeTable().getColumn(name) != null ||
                        network.getDefaultEdgeTable().getColumn(name) != null);
                
                network.getDefaultNodeTable().createColumn(name, Boolean.class, false, false);
                network.getDefaultEdgeTable().createColumn(name, Boolean.class, false, false);

                for (Node node : graph.getNodeList()) {
                    CyNodeAdapter anode = (CyNodeAdapter) node;
                    network.getDefaultNodeTable().getRow(anode.getCyNode().getSUID()).set(name, true);
                }
                for (Edge edge : graph.getEdgeList()) {
                    CyEdgeAdapter aedge = (CyEdgeAdapter) edge;
                    network.getDefaultEdgeTable().getRow(aedge.getCyEdge().getSUID()).set(name, true);
                }
                taskMonitor.setProgress(1D * count * completedOperations / subgraphs.size() / operationCount);
            }
            taskMonitor.setProgress(1D * ++completedOperations / operationCount);
        }
        
        // Add new graphs to collection
        if (config.isNewChild()) {
            CyRootNetworkManager rootManager = getRootNetworkService();
            CyRootNetwork root = rootManager.getRootNetwork(network);
            int count = 0;
            
            
            for (Graph graph : subgraphs) {
                String name;
                do {
                    name = "SG" + (++count);
                } while (network.getDefaultNetworkTable()
                        .getColumn(CyNetwork.NAME).getValues(String.class)
                        .contains(name));
                
                CySubNetwork sub = root.addSubNetwork();
                /* Evyatar & Ariel- adding a new column "Groups" and mapping UI Node to each Node in the Communicator.getSingleton().groups  to give it the correct group, A or B*/
                sub.getTable(CyNode.class,CyNetwork.LOCAL_ATTRS).createColumn("group", String.class, false);
                sub.getTable(CyNode.class,CyNetwork.LOCAL_ATTRS).createColumn("partition number", String.class, false);
                sub.getRow(sub).set(CySubNetwork.NAME, name);
                Map<String, CygrouperNode> m = Communicator.getSingleton().groups.get(count -1);
                graph.getNodeList().stream().forEach(node -> {
                    CyNodeAdapter anode = (CyNodeAdapter) node;
                    sub.addNode(anode.getCyNode());
                    CyRow row =sub.getDefaultNodeTable().getRow(anode.getCyNode().getSUID());
                    CygrouperNode grp = m.get(row.get("name",String.class));                    
                   
                    row.set("group",grp.group);
                    row.set("partition number", grp.kPartiteGroupNumber + "");
                    
                    
                });
                graph.getEdgeList().stream().forEach(edge -> {
                    CyEdgeAdapter aedge = (CyEdgeAdapter) edge;
                    sub.addEdge(aedge.getCyEdge());
                });
                getNetworkManagerService().addNetwork(sub);
                taskMonitor.setProgress(1D * count * completedOperations / subgraphs.size() / operationCount);
            }
            taskMonitor.setProgress(1D * ++completedOperations / operationCount);
        }
        
        if (config.isSaveToFile()) {
            GraphWriter gW = new SimpleGraphIO();
            edu.claflin.finder.Global.setOutput(config.getSaveDirectory());
            
            int count = 0;
            for (Graph graph : subgraphs) {
                gW.writeGraph(graph);
                taskMonitor.setProgress(1D * ++count * completedOperations / subgraphs.size() / operationCount);
            }
            taskMonitor.setProgress(1D * ++completedOperations / operationCount);
        }
        
        taskMonitor.setProgress(1D);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!cancelled && 
                evt.getPropertyName().equals(Algorithm.PROP_PROGRESS)) {
            taskMonitor.setProgress((Double) evt.getNewValue());
        }
    }
    
    @Override
    public void cancel() {
        super.cancel();
        if (taskMonitor != null)
            taskMonitor.setProgress(-1D);
    }
}
