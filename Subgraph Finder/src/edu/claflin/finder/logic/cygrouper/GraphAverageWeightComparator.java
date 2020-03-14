package edu.claflin.finder.logic.cygrouper;

import java.util.Comparator;

import edu.claflin.finder.logic.Graph;

public class GraphAverageWeightComparator implements Comparator<Graph>{
	public int compare(Graph g1, Graph g2) {
		double weight1 = GraphAverageWeight.getAverageWeight(g1);
		double weight2 = GraphAverageWeight.getAverageWeight(g2);
		
		if(weight1 > weight2) {
			return 1;
		}
		else if (weight1 == weight2){
			return 0;
		}		
		else {
			return -1;
		}
	}
}
