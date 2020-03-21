package edu.claflin.finder.logic.cygrouper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.claflin.finder.Global;
import edu.claflin.finder.log.LogLevel;
import edu.claflin.finder.log.LogUtil;
import edu.claflin.finder.logic.Edge;
import edu.claflin.finder.logic.Graph;

public class GraphAverageWeight {
	public static double getAverageWeight(Graph g) {
		double total = 0;
		for(Edge e: g.getEdgeList()) {
			try {				
				int data = (Integer)e.getData();				
				total += data;
			}
			catch(NullPointerException exception) {
				//return -1.0;
			}
			catch(ClassCastException exception) {
				//return -1.0;
			}	
		}
		return total/(g.getEdgeList().size());
	}		
}
