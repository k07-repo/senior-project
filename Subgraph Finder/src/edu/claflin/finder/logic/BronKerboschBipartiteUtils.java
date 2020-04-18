package edu.claflin.finder.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides methods to mark graphs as bipartite using the basic Graph, Edge, and Node classes.
 * Mostly copy-pasted from BipartiteCondition, but modified slightly to be able to mark nodes and return them.
 * To my knowledge the only thing that actually mark nodes uses CytogrouperNode which is a bit different.
 * This class should rectify that.
 * 
 * @author kpuli007
 */
public class BronKerboschBipartiteUtils {
	/**
     * Divides the given graph into bipartite sets.
     * 
     * @return two lists of nodes, each representing a bipartite group.
     * Both lists are empty if the graph is not a valid bipartite graph.
     */
    public static ArrayList<ArrayList<Node>> bipartiteDivision (Graph existingGraph) {
        Set<Node> groupA = new HashSet<>();
        Set<Node> groupB = new HashSet<>();

        for (Node node : existingGraph.getNodeList()) {
            if (!satisfies(existingGraph, groupA, groupB, node)) {
            	//Return empty sets if the graph is not bipartite.
                return constructArrayList(new HashSet<Node>(), new HashSet<Node>());
            }
        }
        
        return constructArrayList(groupA, groupB);
    }

    /**
     * Constructs array list of array lists from sets.
     * 
     * Array lists are used over sets to make traversing the graph's nodes 
     * (see {@link Graph#addEdgesBetweenAllNodesInList(ArrayList)}) possible.
     *
     * @param groupA first output set
     * @param groupB second output set
     * @return array list containing the two sets as array lists
     */
	public static ArrayList<ArrayList<Node>> constructArrayList(Set<Node> groupA, Set<Node> groupB) {
		
		ArrayList<Node> arrA = new ArrayList<Node>();
		for(Node a: groupA) {
			arrA.add(a);
		}
		ArrayList<Node> arrB = new ArrayList<Node>();
		for(Node b: groupB) {
			arrB.add(b);
		}
		
		ArrayList<ArrayList<Node>> result = new ArrayList<ArrayList<Node>>();
		result.add(arrA);
		result.add(arrB);
		return result;
	}
    
    /**
     * Private method for stepping through the adjacency lists and determining 
     * if the graph is bipartite.  Specifically, it uses a Depth-First model 
     * for exploring the node tree and sorting the nodes into two groups.  If 
     * it successfully sorts the nodes into two groups with no invalidating 
     * edges, it returns true.
     * 
     * @param existingGraph the Graph object to test against.
     * @param addSet the current set to add a node to based on the previous.
     * @param compareSet the current set to add adjacent nodes to.
     * @param current the current Node being compared.
     * @return true if the structure could be bipartite.  
     */
    private static boolean satisfies(Graph existingGraph, Set<Node> addSet, Set<Node> compareSet, Node current) {
        if (!addSet.contains(current) && !compareSet.contains(current)) {
            addSet.add(current);
            List<Node> adjacent = existingGraph.getAdjacencyList(current);
            for (Node neighbor : adjacent) {
                if (addSet.contains(neighbor) || 
                        !satisfies(existingGraph, compareSet, addSet, neighbor))
                    return false;
            }
        }
        
        return true;
    }
}
