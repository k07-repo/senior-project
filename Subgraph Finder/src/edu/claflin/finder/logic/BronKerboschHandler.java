package edu.claflin.finder.logic;

import edu.claflin.finder.logic.Graph;
import edu.claflin.finder.logic.Node;
import edu.claflin.finder.logic.Edge;
import java.util.*;

/**
 * Code for finding maximal clique of a graph using the Bron-Kerbosch algorithm.
 * https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
 * 
 * This was originally meant to be used for maximum complete bipartite graphs
 * but I didn't figure out how to do that yet
 * 
 * Somewhat of a WIP.
 */
public class BronKerboschHandler {
	
	/**
	 * Given a graph, finds all complete subgraphs, and then returns the largest one.
	 * 
	 * @param graph the graph
	 * @return the largest complete subgraph (clique)
	 */
	public static Graph maximumCompleteClique(Graph graph) {
        System.out.println("---------------------");

        ArrayList<Graph> completeGraphs = bronKerbosch(graph);

        Graph maximumGraph = null;
        int maximumSize = 0;
        for(int k = 0; k < completeGraphs.size(); k++) {
            Graph current = completeGraphs.get(k);
            if(current.getNodeList().size() > maximumSize) {
                maximumGraph = current;
                maximumSize = current.getNodeList().size();
            }
        }

        return maximumGraph;

        //This is largest complete CLIQUE.
    }
	
	/**
	 * Given a graph, finds all complete subgraphs.
	 * 
	 * @param graph the graph
	 * @return an ArrayList of complete subgraphs, unordered
	 */
	public static ArrayList<Graph> bronKerbosch(Graph graph) {
		ArrayList<Graph> results = new ArrayList<>();
        bronKerbosch(results, new Graph("r"), graph, new Graph("x"));
        return results;
	}
	
	/**
	 * Recursive helper method to do the above.
	 * 	
	 * @param results result list. Passed between recursive calls to create output
	 * @param r graph r in wikipedia link
	 * @param p graph p in wikipedia link
	 * @param x graph x in wikipedia link
	 */
	public static void bronKerbosch(ArrayList<Graph> results, Graph r, Graph p, Graph x) {
        //AS OF NOW, RESULTING GRAPH R HAS NO EDGES
        //Ideally after algorithm is finished we just manually transpose the edgees with one swift pass
        //System.out.println(r + "\n" + p + "\n " + x + "\n");
        if(p.getNodeCount() <= 0 && x.getNodeCount() <= 0) {
            results.add(r.uniqueCopy());
            return;
        }
        else {
            //Clone list before using to avoid concurrent modification exception
            ArrayList<Node> pNodeList = p.uniqueCopyNodeList();
           
            //Iterate through each vertex in the list
            for(Node n: pNodeList) {
                //Clone graphs because otherwise we are modifying the original graphs
                Graph r2 = r.uniqueCopy();
                Graph p2 = p.uniqueCopy();
                Graph x2 = x.uniqueCopy();

                //Call algorithm on union(r2, n), intersection(p2, neighbors(n)), intersection(x2, neighbors(n)).
                //These functions should not affect the original graphs, thus the cloning
                //Neighbors uses the base (p2) here, this is important for now
               
               
                r2.addNode(n);                    
                p2.intersect(p2.getAdjacencyList(n));
                x2.intersect(p2.getAdjacencyList(n));
                bronKerbosch(results, r2, p2, x2);

                //Modify the original graphs for the next loop
                //Remove node n from the original p, and then add n to r
                p.removeNode(n);
                x.addNode(n);
            }
        }
    }
}
