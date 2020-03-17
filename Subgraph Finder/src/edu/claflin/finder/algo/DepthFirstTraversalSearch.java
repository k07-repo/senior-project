package edu.claflin.finder.algo;

import static edu.claflin.finder.Global.getLogger;
import edu.claflin.finder.log.LogLevel;
import edu.claflin.finder.logic.ConditionedGraph;
import edu.claflin.finder.logic.Graph;
import edu.claflin.finder.logic.Edge;
import edu.claflin.finder.logic.Node;
import edu.claflin.finder.logic.PrioritySet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Processes a {@link Graph} searching for bipartite subgraphs by performing a 
 * depth first search on each node.
 * 
 * @author Charles Allen Schultz II
 * @version 3.4 February 4, 2016
 */
public class DepthFirstTraversalSearch extends Algorithm {

    /**
     * Public constructor for initializing the DepthFirstTraversalSearch with 
     * default conditions.
     * @param bundle the ArgumentsBundle containing the instantiation arguments.
     */
    public DepthFirstTraversalSearch(ArgumentsBundle bundle) {
        super(bundle);
        
        if (getLogger() != null) {
            getLogger().logInfo(LogLevel.DEBUG, "Depth First Traversal Search "
                    + "algorithm initialized.");
        }
    }

    /**
     * {@inheritDoc }
     * <br>
     * Finds most Subgraphs by performing a depth first search on the node 
     * tree.  It is unknown if it finds all or only most of the subgraphs due 
     * to the nature of the algorithm.  It is expected, however, that it would 
     * miss certain node groupings.
     * 
     * @param graph the {@link Graph} object to search through.
     * @return the ArrayList of Graph objects holding all found subgraphs.
     */
    @Override
    public ArrayList<Graph> process(Graph graph) {
        ArrayList<Graph> subGraphs = new ArrayList<>();
        
        if (getLogger() != null) {
            getLogger().logAlgo(LogLevel.NORMAL, 
                    "DFTS: Searching Graph: " + graph.getName());
        }
        
        for (Node current : graph.getNodeList()) {
            
            if (getLogger() != null) {
                getLogger().logAlgo(LogLevel.VERBOSE, 
                        "DFTS: Setting Node as root: " + current.toString());
            }
            
            Graph subGraph = new ConditionedGraph("[DFS]_N[" + current + "]_" + 
                graph.getName(), args.getConditionsList());
            ArrayList<Node> visited = new ArrayList<>();
            visited.add(current);
            subGraphs.add(searchNode(graph, subGraph, current, visited));
            setProgress(graph.getNodeIndex(current) * 1D / graph.getNodeCount());
        }
        
        if (getLogger() != null) {
            getLogger().logAlgo(LogLevel.NORMAL, 
                    "DFTS: Finished Searching Graph. SGs found: "
                    + subGraphs.size());
        }
        
        return cull(subGraphs);
    }
    /**
     * Helper method to search for the SubGraphs.  Loops recursively 
     * to search for subgraphs.  Is called on each node in the tree.
     * 
     * @param graph the Graph object to search through.
     * @param subGraph the Graph object indicating the subgraph to store found 
     * nodes and edges in.  (Used due to the recursive nature of the algorithm.)
     * @param node the current node to search.
     * @param visited the List containing the visited nodes.
     * @return the Graph object representing the found subgraph.
     */
    private Graph searchNode(Graph graph, Graph subGraph, Node node, List<Node> visited) {
        if (!subGraph.getNodeList().contains(node))
            subGraph.addNode(node);
        
        // Check to see if the current node has any edges back into the 
        // graph and attempt add them in sequence.
        // NOT unnecessary if the algorith is NOT preservative.
        List<Node> cAdjacency = graph.getAdjacencyList(node);
        List<Edge> cEdges = new ArrayList<>();
        cAdjacency.retainAll(subGraph.getNodeList());
        cAdjacency.stream().forEach(n -> {
            cEdges.add(graph.getEdge(node, n));
        });
        cEdges.removeAll(subGraph.getEdgeList());
        cEdges.stream().forEach(e -> subGraph.addEdge(e));
        
        // Crazy queue mechanism for setting the ordering of explored nodes.
        Queue<Edge> queue;
        Comparator<Edge> comparator = null;
        try {
            Object obj = args.getObject(ArgumentsBundle.COMMON_ARGS.EDGE_WEIGHT_COMPARATOR.toString());
            if (obj != null) {
                comparator = (Comparator<Edge>) obj;
            }
        } catch (ClassCastException e) { // In future, maybe change ArgumentsBundle to auto-cast things appropriately and restrict elements.
            if (getLogger() != null) {
                getLogger().logAlgo(LogLevel.NORMAL, "BFTS: Error casting EDGE_WEIGHT_COMPARATOR");
            }
        }
        if (comparator != null)
            queue = new PrioritySet<>(comparator, true);
        else
            queue = new LinkedList<>();
        graph.getAdjacencyList(node).stream()
                .forEach(n -> {
                    Edge e = graph.getEdge(node, n);
                    if (!visited.contains(n))
                        queue.add(e);
                });
        
        Boolean preservative = args.getBoolean(ArgumentsBundle.COMMON_ARGS.EDGE_PRESERVATION.toString());
        
        while (!queue.isEmpty()) {
            Edge currentEdge = queue.remove();
            Node neighbor;
            if (currentEdge.isUndirected()) {
                if (!visited.contains(currentEdge.getSource())) {
                    neighbor = currentEdge.getSource();
                } else if (!visited.contains(currentEdge.getDestination())) {
                    neighbor = currentEdge.getDestination();
                } else {
                    continue; // eat the edge since it's already been "explored"
                }
            } else {
                neighbor = currentEdge.getDestination();
            }
            
            List<Node> nList = new ArrayList<>();
            List<Edge> eList = new ArrayList<>();
            nList.add(neighbor);
            eList.add(graph.getEdge(node, neighbor));
            
            // If preservative, add a node and all it's edges back into the 
            // graph all at once.
            if (preservative != null && preservative) {
                List<Node> nAdjacency = graph.getAdjacencyList(neighbor);
                nAdjacency.retainAll(subGraph.getNodeList());
                nAdjacency.stream().forEach(n -> {
                    Edge e = graph.getEdge(neighbor, n);
                    eList.add(e);
                });
            }
            
            // Remove any allowable additions that are already present.
            nList.removeAll(subGraph.getNodeList());
            eList.removeAll(subGraph.getEdgeList());
            
            // Do the addition, and, if successful, recurse on the node.
            if (subGraph.addPartialGraph(nList, eList)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    searchNode(graph, subGraph, neighbor, visited);
                }
            }
        }
        
        return subGraph;
    }
}
