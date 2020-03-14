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
				writeToFile((e.getData() == null) + "");
				writeToFile(e.getSource() + " " + e.getDestination() + " " + e.getData());
				total += data;
			}
			catch(NullPointerException exception) {
				
				//return -1.0;
				
			}
			catch(ClassCastException exception) {
				//return -1.0;
			}	
			catch (IOException ex) {
				System.exit(-1);
			}
			
		}
		return total/(g.getEdgeList().size());
	}
	
	public static void writeToFile(String s) throws IOException {
		File file = new File("outputLogMustBeFound.txt");
		if(!file.exists()) {
			file.createNewFile();
		}
		LogUtil.path = file.getAbsolutePath();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.append(s);
		writer.close();
	}
}
