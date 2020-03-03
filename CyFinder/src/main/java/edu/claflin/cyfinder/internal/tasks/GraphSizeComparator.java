package edu.claflin.cyfinder.internal.tasks;

import java.util.Comparator;

import edu.claflin.finder.logic.Edge;
import edu.claflin.finder.logic.Graph;
import edu.claflin.finder.logic.Node;

public class GraphSizeComparator implements Comparator<Graph> {
	public int compare(Graph g1, Graph g2) {
		
		int count1 = g1.getNodeCount();
		int count2 = g2.getNodeCount();
		
		if(count1 > count2) {
			return 1;
		}
		else if (count1 == count2){
			return 0;
		}		
		else {
			return -1;
		}
		
	}
}
