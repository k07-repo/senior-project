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
package edu.claflin.cyfinder.internal.logic;

import edu.claflin.finder.logic.Edge;
import org.cytoscape.model.CyEdge;

/**
 * Represents an adaptation of the CyEdge to the finder Edge
 * @author Charles Allen Schultz II
 * @version 1.1 February 23, 2016
 */
public class CyEdgeAdapter extends Edge {

    /**
     * The CyNode this Finder Node represents.
     */
    private final CyEdge cyedge;
    
    /**
     * Constructs the Adapter
     * @param cyedge the CyNode to point the Finder Node to.
     */
    public CyEdgeAdapter(CyNodeAdapter node1, CyNodeAdapter node2, Object data, 
            CyEdge cyedge) {
        super(node1, node2, data, !cyedge.isDirected());
        this.cyedge = cyedge;
    }
    
    /**
     * Returns the CyEdge this Edge represents.
     * @return the CyEdge this Edge represents.
     */
    public CyEdge getCyEdge() {
        return cyedge;
    }
}
