package edu.claflin.finder.logic.cond;


import edu.claflin.finder.logic.Condition;

import edu.claflin.finder.logic.Graph;
import edu.claflin.finder.logic.Node;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a bipartite graph as a Condition object.  Used to test for 
 * bipartiteness.
 * 
 * @author Charles Allen Schultz II
 * @version 1.1.1 May 26, 2015
 */
public class BipartiteCondition extends Condition {

    

    public BipartiteCondition(){
    }
    
    /**
     * {@inheritDoc }
     * <p>
     * Checks to determine if the supplied graph is Bipartite.
     * @return true if the graph is bipartite.
     */
    @Override
    public boolean satisfies(Graph existingGraph) {
        Set<Node> groupA = new HashSet<>();
        Set<Node> groupB = new HashSet<>();

        for (Node node : existingGraph.getNodeList()) {
            if (!satisfies(existingGraph, groupA, groupB, node))
                return false;
        }
        return true;
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
    private boolean satisfies(Graph existingGraph, Set<Node> addSet, Set<Node> compareSet, Node current) {
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
