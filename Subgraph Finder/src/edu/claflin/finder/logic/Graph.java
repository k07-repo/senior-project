package edu.claflin.finder.logic;

import static edu.claflin.finder.Global.getLogger;
import edu.claflin.finder.log.LogLevel;

import java.util.*;

/**
 * Used to represent a graph in memory. Migrated from matrix form, the graph is 
 * now kept track of via a list of edges and nodes.  All the previous methods 
 * remain in existence but the underlying implementation has changed.
 * 
 * @author Charles Allen Schultz II
 * @version 3.4 February 2, 2016
 */
public class Graph {
    /**
     * The name of the Graph provided by the user.  This value is used
     * by the program to name output files unless specified otherwise.
     */
    private final String graphName;
    
    /**
     * The list of the nodes added to the graph.  Used by the program to keep
     * track of the associations within the matrix.  It may be re-ordered
     * dynamically at runtime.
     */
    private final ArrayList<Node> nodeList;
    
    /**
     * The list of edges added to the graph.  The graph is, by default, a 
     * directed graph.  As such, two edges must be added to force the graph to 
     * be bidirectional.
     */
    private final ArrayList<Edge> edgeList;
    
    /**
     * Used to suppress logging.
     */
    protected boolean suppressLog = false;
    
    /**
     * Initializes the graph.  Merely initializes fields.  It does not set any
     * data.  Also sets the Logging Utility object.  If null, the class will not
     * log data.
     * 
     * @param graphName the String which represents the name of the Graph.
     */
    public Graph(String graphName) {
        this.graphName = graphName;
        this.nodeList = new ArrayList();
        this.edgeList = new ArrayList();
    }
    /**
     * Private constructor used by subGraph method.  
     * 
     * @param graphName the String which represents the name of the Graph.
     * @param nodeList the ArrayList of Node objects.
     * @param edgeList the ArrayList of Edge objects.
     */
    protected Graph(String graphName, ArrayList<Node> nodeList, 
            ArrayList<Edge> edgeList) {
        this.graphName = graphName;
        this.nodeList = nodeList;
        this.edgeList = edgeList;
    }
    
    /**
     * Gets the name of the graph.
     * 
     * @return The graph name.
     */
    public String getName() {
        return graphName;
    }
    
    /**
     * Adds a node to the graph.  Adds the specified node name to the nodeList 
     * by creating and adding a new node and then calling the addNode(Node) 
     * method.
     * 
     * @param node the String which denotes the name of the node to add.
     * @deprecated Outdated since v3. Replaced by 
     * {}
     * @return true if the addition was successful.
     */
    public boolean addNewNode(String node) {
        return addNode(new Node(node));
    }
    /**
     * Adds a node to the graph.  Adds a Node object to the graph.
     * @param node the Node object to add.
     * @return true if the addition was successful.
     */
    public boolean addNode(Node node) {
        return addNodes(Arrays.asList(node));
    }
    /**
     * Adds a list of nodes to the graph.
     * @param nodes the List&lt;Node&gt; containing the nodes to add.
     * @return true if the addition was successful.
     */
    public boolean addNodes(List<Node> nodes) {
        return addPartialGraph(nodes, null);
    }
    /**
     * Gets the index of a node based on its name. Maintained with the new 
     * implementation to preserve existing uses.  Will not match nodes which 
     * may have additional properties attached in future implementations.
     * 
     * @param node the String representing the node to search for.
     * @deprecated Outdated since v3. Replaced by 
     * {}
     * @return the index of the node or -1 if the node was not found.
     */
    public int getNodeIndex(String node) {
        return getNodeIndex(new Node(node));
    }
    /**
     * Gets the index of a Node object in memory.  Retained for older API 
     * compatibility.  The ordering of the nodes is not guaranteed and may 
     * change during the program's runtime.
     * @param node the Node object to search for.
     * @return the index of the node or -1 if the node was not found.
     */
    public int getNodeIndex(Node node) {
        return nodeList.indexOf(node);
    }
    /**
     * Gets the number of nodes in the graph.
     * 
     * @return the integer representing the total number of nodes in the graph.
     */
    public int getNodeCount() {
        return nodeList.size();
    }
    /**
     * Gets the name of the node at the specified nodeIndex.  Ensures that the
     * provided integer is within the range of available indices.
     * 
     * @param nodeIndex the integer representing the index to get from the 
     * headerList.
     * @deprecated Outdated since v3.  No replacement. Obtain the Node data via 
     * the {@link Node} object.
     * @return the String representing the name of the node.
     */
    public String getNodeName(int nodeIndex) {
        checkNodeIndex(nodeIndex);
        return nodeList.get(nodeIndex).getIdentifier();
    }
    /**
     * Returns the list of nodes in the graph.
     * 
     * @return the List&lt;Node&gt; containing the list of nodes in the graph.
     */
    public List<Node> getNodeList() {
        List<Node> newList = new ArrayList<>();
        newList.addAll(nodeList);
        return newList;
    }
    
    /**
     * Adds an edge connecting the specified nodes in the Graph.  Sets the
     * object data for the edge dependent upon the supplied parameter.
     * 
     * @param node1Index the integer value indicating the first node.
     * @param node2Index the integer value indicating the second node.
     * @param edgeData the Object that represents unique edge data (i.e. weight
     * or edge relationship).
     * @deprecated Outdated since v3.  Replaced by
     * {}
     * @return true if the addition was successful.
     */
    public boolean addEdge(int node1Index, int node2Index, Object edgeData) {
        checkNodeIndex(node1Index);
        checkNodeIndex(node2Index);
        
        // Deprecated command ONLY creates directed edges.
        Edge edge = new Edge(nodeList.get(node1Index), nodeList.get(node2Index), edgeData, false);
        return addEdge(edge);
    }
    /**
     * Adds an edge connecting the specified nodes to the Graph.  This is a
     * convenience method for adding an edge using node names.  Graphs are
     * undirected so the order of the parameters does not matter.
     * 
     * @param node1 the String indicating the first node.
     * @param node2 the String indicating the second node.
     * @param edgeData the Object that represents unique edge data (i.e. weight
     * or edge relationship).
     * @deprecated Outdated since v3.  Replaced by
     * {
     * @return true if the addition was successful.
     */
    public boolean addEdge(String node1, String node2, Object edgeData) {
        int node1Index = getNodeIndex(node1);
        int node2Index = getNodeIndex(node2);
        
        return addEdge(node1Index, node2Index, edgeData);
    }
    /**
     * Adds an edge to the Graph.  If any edge already exists within the Graph 
     * connecting the two supplied nodes or either of the two nodes is not 
     * in the nodeList, then the edge is not added.
     * 
     * @param edge the Edge object to add to the graph.
     * @return true if the addition was successful.
     */
    public boolean addEdge(Edge edge) {
        return addEdges(Arrays.asList(edge));
    }
    /**
     * Adds a list of Edges to the graph.
     * @param edges the List&lt;Edge&gt; containing the edges to add.
     * @return true if the addition was successful.
     */
    public boolean addEdges(List<Edge> edges) {
        return addPartialGraph(null, edges);
    }
    /**
     * Gets the value of the edge between the specified nodes.
     * 
     * @param node1Index the integer value of the first node.
     * @param node2Index the integer value of the second node.
     * @deprecated Outdated since v3. No replacement.  Obtain the edge first, 
     * then obtain the data from the Edge object.
     * @return the Object representing the edge relationship.
     */
    public Object getEdge(int node1Index, int node2Index) {
        checkNodeIndex(node1Index);
        checkNodeIndex(node2Index);
        
        Edge fakeEdge = getEdge(nodeList.get(node1Index), nodeList.get(node2Index));
        
        return fakeEdge.getData();
    }
    /**
     * Gets the value of the edge between the specified nodes.  This is a
     * convenience method for getting the edge using the node's String name.
     * 
     * @param node1 the String of the first node.
     * @param node2 the String of the second node.
     * @deprecated Outdated since v3. No replacement.  Obtain the edge first, 
     * then obtain the data from the Edge object.
     * @return the Object representing the edge relationship.
     */
    public Object getEdge(String node1, String node2) {
        int node1Index = getNodeIndex(node1);
        int node2Index = getNodeIndex(node2);
        
        Edge fakeEdge = getEdge(nodeList.get(node1Index), nodeList.get(node2Index));
        
        return fakeEdge.getData();
    }
    /**
     * Locates an Edge object in the edgeList and returns it.  Note that it will 
     * also return an edge with the provided source and destination parameters 
     * swapped IF AN ONLY IF said edge is undirected.
     * 
     * @param source the source Node of the edge.
     * @param destination the destination Node of the edge.
     * @return the Edge object or null if not found.
     */
    public Edge getEdge(Node source, Node destination) {
        // "fake" access is accessing an edge utilizing only the source and 
        // destination.  the equals method on edges do not check for data nor
        // undirectedness since edges with the same source and destination are 
        // considered equal. 
        
        // Locate un/directed edge via "fake" access.
        int index = edgeList.indexOf(new Edge(source, destination, null, false));
        if (index != -1)
            return edgeList.get(index);
        
        // Locate undirected reversed node edges also via "fake" access.
        index = edgeList.indexOf(new Edge(destination, source, null, false));
        if (index != -1 && edgeList.get(index).isUndirected())
            return edgeList.get(index);
        
        return null;
    }
    /**
     * Returns the list of edges in the graph.
     * 
     * @return the List&lt;Edge&gt; containing the list of edges in the graph.
     */
    public List<Edge> getEdgeList() {
        List<Edge> newList = new ArrayList<>();
        newList.addAll(edgeList);
        return newList;
    }
    /**
     * Adds a partial graph to this graph.  Specifically adds a set of nodes 
     * and a set of edges to the graph.  It should be noted that nodes or 
     * edges in the graph already that attempt to be added again may cause 
     * exceptions to occur.  Additionally, Nodes are added before Edges so any 
     * Edges that depend on Nodes in the add list may be safely added 
     * simultaneously.  This method is the backbone structure for adding any 
     * component to the graph as all other addition methods filter through here.
     * 
     * @param nodes the List&lt;Node&gt; containing the nodes to add.
     * @param edges the List&lt;Edge&gt; containing the edges to add.
     * @return true, always.  Allows overriding in subclasses which wish to 
     * change base implementation.
     */
    public boolean addPartialGraph(List<Node> nodes, List<Edge> edges) {
        if (nodes != null)
            nodes.stream()
                    .forEach(node -> {
                        checkNode(node);
                        nodeList.add(node);
                        if (!suppressLog && getLogger() != null) {
                            getLogger().logGraph(LogLevel.VERBOSE, getName()
                                    + ": Added Node: \"" + node + "\"");
                        }
                    });
        
        if (edges != null)
            edges.stream()
                    .forEach(edge -> {
                        if (nodeList.containsAll(Arrays.asList(edge.getSource(), edge.getDestination())) &&
                                !edgeList.contains(edge)) {
                            edgeList.add(edge);
                            if (!suppressLog && getLogger() != null) {
                                getLogger().logGraph(LogLevel.VERBOSE, getName()
                                        + ": Added Edge: " + edge);
                            }
                        }
                    });
        return true;
    }
    /**
     * Returns a subGraph of this graph.  Isolates and extracts a subgraph 
     * based on user supplied values.  The new Graph is a separate Graph object.
     * 
     * @param startNode the integer pointing to the start node.
     * @param stopNode the integer pointing to the stop node.
     * @param nameQualifier the String representing an additional qualifier to
     * prepend the name of the new Graph object with.
     * @deprecated Outdated since v3.  Replaced by
     * {@link #getSubGraph(java.util.List, java.lang.String)}
     * @return the Graph object representing a subgraph of the original.
     */
    public Graph getSubGraph(int startNode, int stopNode, String nameQualifier) {
        List<Node> subList = nodeList.subList(startNode, stopNode);
        
        String name = nameQualifier + "S[" + startNode + "," + stopNode + "]" + 
                "-";
        
        return getSubGraph(subList, name);
    }
    /**
     * Returns a subGraph of this graph.  Isolates and extracts a subgraph 
     * based on user supplied values.  The new Graph is a separate Graph object.
     * 
     * @param subStringSet an ArrayList containing the String names of the desired nodes.
     * @param nameQualifier the String representing an additional qualifier to 
     * prepend the name of the new Graph object with.
     * @deprecated Outdated since v3.  Replaced by
     * {@link #getSubGraph(java.util.List, java.lang.String)}
     * @return the Graph object representing a subgraph of the original.
     */
    public Graph getSubGraph(ArrayList<String> subStringSet, String nameQualifier) {
        
        List<Node> mainList = getNodeList();
        List<Node> subList = new ArrayList<>();
        subStringSet.stream().forEach(nodeString -> 
                mainList.stream()
                        .filter(node -> node.getIdentifier().equals(nodeString))
                        .forEach(node -> subList.add(node))); // Should only add one.
        
        return getSubGraph(subList, nameQualifier);
    }
    /**
     * Returns a subGraph of this graph.  Isolates and extracts a subgraph 
     * based on a user supplied node list.  The new Graph is a quasi-separate 
     * Graph.  Each node utilized is directly related between the two graphs 
     * and, as such, changes to the properties of one node in the subgraph 
     * will be reflected in the super graph.  To obtain an independent graph 
     *
     * 
     * @param nodes the List&lt;Node&gt; containing the Node references.
     * @param nameQualifier the String to differentiate the Graph names.
     * @return the Graph representing the subgraph.
     */
    public Graph getSubGraph(List<Node> nodes, String nameQualifier) {
        ArrayList<Node> nodeSubList = new ArrayList<>();
        ArrayList<Edge> edgeSubList = new ArrayList<>();
        nodeSubList.addAll(nodes);
        
        nodes.stream().forEach((edu.claflin.finder.logic.Node node) -> {
            List<Node> adjacency = getAdjacencyList(node);
            adjacency.retainAll(nodes);
            adjacency.stream().forEach((edu.claflin.finder.logic.Node dest) -> {
                edgeSubList.add(getEdge(node, dest));
            });
        });
        
        String name = nameQualifier + graphName;
        
        return new Graph(name, nodeSubList, edgeSubList);
    }
    
    /**
     * Transposes the specified nodes.  Swaps the location of the nodes in
     * memory.  Specifically swaps the nodes in the nodeList as the Edge data 
     * has been uncoupled from the Node data.  Newer implementation is 
     * dependent upon the int based method for performance.
     * 
     * @param node1Index the integer index of the first node.
     * @param node2Index the integer index of the second node.
     */
    public void transpose(int node1Index, int node2Index) {
        checkNodeIndex(node1Index);
        checkNodeIndex(node2Index);
        
        Node a = nodeList.get(node1Index);
        Node b = nodeList.get(node2Index);
        nodeList.set(node2Index, a);
        nodeList.set(node1Index, b);
        
        if (!suppressLog && getLogger() != null) {
            getLogger().logGraph(LogLevel.VERBOSE, 
                    getName() + ": Transposed Nodes \"" + 
                    nodeList.get(node1Index) + "\" & \"" + 
                    nodeList.get(node2Index) + "\".");
        }
    }
    /**
     * Transposes the specified nodes.  Swaps the location of the nodes in
     * memory.  This is a convenience method for transposing the nodes based on
     * the Sting representation of the nodes.
     * 
     * @param node1 the String name of the first node.
     * @param node2 the String name of the second node.
     * @deprecated Outdated since v3. Replaced by
     * {}
     */
    public void transpose(String node1, String node2) {
        int node1Index = getNodeIndex(node1);
        int node2Index = getNodeIndex(node2);
        
        transpose(node1Index, node2Index);
    }
    /**
     * Transposes the specified nodes.  Relies on the integer based method for 
     * carrying out the operation.
     * 
     * @param node1 the Node object in the first position.
     * @param node2 the Node object in the second position.
     */
    public void transpose(Node node1, Node node2) {
        transpose(nodeList.indexOf(node1), nodeList.indexOf(node2));
    }
    
    /**
     * Obtains an adjacency list for the supplied node based on the Graph.
     * 
     * @param node the Node to obtain the adjacency list for.
     * @return the List&lt;Node&gt; containing the adjacent Nodes. 
     */
    public List<Node> getAdjacencyList(Node node) {
        List<Node> adjacent = new ArrayList<>();
        
        Iterator<Edge> edgeIterator = edgeList.iterator();
        while (edgeIterator.hasNext()) {
            Edge edge = edgeIterator.next();
            if (edge.getSource().equals(node)) {
                adjacent.add(edge.getDestination());
            } else if (edge.isUndirected() && edge.getDestination().equals(node)) {
                adjacent.add(edge.getSource());
            }
        }
        
        return adjacent;
    }
    
    /**
     * Verifies that the node is NOT in the graph.  Checks to see if the 
     * provided node name is in the headerList and, hence, the dataMatrix.  This
     * is an internal method used to ensure that the algorithm is operating
     * correctly.
     * 
     * @param node the Node to check for.
     */
    private void checkNode(Node node) {
        if(getNodeIndex(node) != -1) {
            String errorString = node + " is already in the graph!";
            
            if (!suppressLog && getLogger() != null) {
                getLogger().logError(LogLevel.NORMAL, 
                        getName() + ": " + errorString);
            }
            
            throw new IllegalArgumentException(errorString);
        }
    }
    /**
     * Verifies that the nodeIndex is valid.  Ensures that the node does not
     * fall out of bounds.  This is an internal method used to ensure that the
     * algorithm is operating correctly.
     * 
     * @param nodeIndex the integer of the node to check for.
     * @deprecated No replacement.  Further operations shall utilize
     * {)}
     */
    private void checkNodeIndex(int nodeIndex) {
        if (nodeList.size() >= nodeIndex && nodeIndex < 0) {
            String errorString = nodeIndex + " is not a valid Header index!";
            
            if (!suppressLog && getLogger() != null) {
                getLogger().logError(LogLevel.NORMAL, 
                        getName() + ": " + errorString);
            }
            
            throw new ArrayIndexOutOfBoundsException(errorString);
        }
    }
    
    /**
     * Copies this Graph object using the getSubGraph() method.
     * 
     * @return a Graph object identical to the original.
     */
    public Graph copy() {
        return getSubGraph(nodeList, "");
    }
    /**
     * Copies this Graph producing a unique copy in which there is no 
     * entanglement between the two Graph objects.  I.e. there are no shared 
     * nodes or edges between the two graphs.  Mathematically speaking:
     * <code>x.getNodeList().get(0) != y.getNodeList().get(0)</code> under any 
     * circumstances.  The Nodes and Edges may remain equivalent via the
     * equals() implementation.  Further, Edge data shall remain entangled due 
     * to implementation details.
     * 
     * @return a unique copy of the graph object.
     */
    public Graph uniqueCopy() {
        ArrayList<Node> nodeList = new ArrayList<>();
        ArrayList<Edge> edgeList = new ArrayList<>();
        
        this.nodeList.stream()
                .forEach(node -> nodeList.add(node.duplicate()));
        this.edgeList.stream()
                /* This is gonna look crazy, but network components may be 
                equivalent even if they are not the same object in memory. This 
                means we can obtain the new Node from the new list by utilizing 
                its equivalency with the old Node in the old list.*/
                .forEach(edge -> {
                    Node newSource = nodeList.get(nodeList.indexOf(edge.getSource()));
                    Node newDest = nodeList.get(nodeList.indexOf(edge.getDestination()));
                    edgeList.add(edge.duplicate(newSource, newDest));
                });
        
        return new Graph(graphName, nodeList, edgeList);
    }
    
    /**
     * Clone method that only clones the node list, done for convenience because lists don't have built in cloning.
     *      *
     * @return a unique copy of the graph's node list
     */
    public ArrayList<Node> uniqueCopyNodeList() {
    	 ArrayList<Node> nodeList = new ArrayList<>();
    	 this.nodeList.stream().forEach(node -> nodeList.add(node.duplicate()));
    	 return nodeList;
    }
    
    /**
     * Intersects this graph's node list with the input list.
     * 
     * @param nodeList the list to intersect with
     */
    public void intersect(List<Node> nodeList) {
    	this.getNodeList().retainAll(nodeList);
    }
    
    /**
     * Removes a node from the set. Only used with the Bron-Kerbosch algorithm right now, hopefully doesn't break anything
     * Does NOT affect edges!!!!!!! At time of writing may cause issues if you rely on it to do so
     * 
     * @param node the node to remove
     */
    public void removeNode(Node node) {
    	for(Node current: nodeList) {
    		if(current.equals(node)) {
    			nodeList.remove(current);
    		}
    	}    	
    }
    
    /**
     * Adds all edges containing the input node from the input graph to this graph,
     * provided that the other vertex is already within this graph.
     * Probably not very efficient
     * 
     * @param graph the graph
     * @param node the node
     */
    public void transferEdgesContainingNodeFromGraph(Graph graph, Node node) {
    	for(Edge e: graph.getEdgeList()) {
            if(e.getSource().equals(node) && this.containsNode(e.getDestination()) ||
            		e.getDestination().equals(node) && this.containsNode(e.getSource())) {
                this.edgeList.add(e);
            }
        }
    }
    
    /**
     * Checks if the graph contains the given node.
     * 
     * @param node the node to check for
     * @return whether or not node is in this graph
     */
    public boolean containsNode(Node node) {
    	for(Node n: this.getNodeList()) {
    		if(n.equals(node)) {
    			return true;
    		}
    	}
    	return false;
    }
    

    //BRON KERBOSCH BIPARTITE STARTS HERE 
    

    /**
     * Adds edges in between all possible pair combinations of nodes in a list
     * if both nodes involved are present in the graph.
     * 
     * @param list The list of nodes
     */
    public void addEdgesBetweenAllNodesInList(ArrayList<Node> list) {
        for(int k = 0; k < list.size(); k++) {
            for(int j = k + 1; j < list.size(); j++) {
            	Node first = list.get(j);
            	Node second = list.get(k);
            	if(this.containsNode(first) && this.containsNode(second)) {
            		this.edgeList.add(new Edge(first, second, "", false));
            	}         
            }
        }
    }

    /**
     * Removes edges in between all possible pair combinations of nodes in a list
     * if both nodes involved are present in the graph.
     * 
     * When used with Bron-Kerbosch bipartite, this removes all nodes since
     * the precondition is that this is only used on the nodes in a single group,
     * which are not supposed to have any edges to begin with.
     * 
     * @param list The list of nodes
     */
    public void removeEdgesBetweenAllNodesInList(ArrayList<Node> list) {
        for(int k = 0; k < list.size(); k++) {
            for(int j = k + 1; j < list.size(); j++) {
                Edge toRemove = null;
                for(Edge e: edgeList) {
                    Node src = list.get(k);
                    Node dest = list.get(j);
                    if(((e.getSource().equals(src) && e.getDestination().equals(dest) ||
                    		(e.getSource().equals(dest) && e.getDestination().equals(src))))) {
                        toRemove = e;
                        break;
                    }
                }                
                
                //Removal outside of the loop to prevent ConcurrentModificationException
                if(toRemove != null) {
                    edgeList.remove(toRemove);
                }
            }
        }
    }
}
