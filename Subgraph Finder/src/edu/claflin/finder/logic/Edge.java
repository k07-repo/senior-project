package edu.claflin.finder.logic;

import static edu.claflin.finder.Global.getLogger;
import edu.claflin.finder.log.LogLevel;
import java.util.Objects;

/**
 * Represents an Edge in memory.  A simple implementation requiring only
 * two nodes and a single piece of edge data.
 * 
 * @author Charles Allen Schultz II
 * @version 3.1 February 2, 2016
 * @param <D> the data type of the data attached to the edge object.
 */
public class Edge<D> {
    /**
     * The node representing the source.
     */
    private final Node source;
    /**
     * The node representing the destination.
     */
    private final Node destination;
    /**
     * The data attached to the edge.
     */
    private D data;
    /**
     * Indicates if this edge should be treated as being undirected.
     */
    private boolean undirected;

    /**
     * Initializes the Edge object.
     *
     * @param source the Node representing the interaction source.
     * @param destination the Node representing the interaction destination.
     * @param data the Object representing the Edge data.
     * @param undirected the boolean indicating if this edge is undirected.
     */
    public Edge(Node source, Node destination, D data, boolean undirected) {
        this.source = source;
        this.destination = destination;
        this.data = data;
        this.undirected = undirected;
        
        if (getLogger() != null) {
            getLogger().logInfo(LogLevel.DEBUG, "Edge: Created Edge: "
                    + toString());
        }
    }

    /**
     * Access method for the Edge's source.
     * @return the Node representing the source.
     */
    public Node getSource() {
        return source;
    }

    /**
     * Access method for the Edge's destination.
     * @return the Node representing the destination.
     */
    public Node getDestination() {
        return destination;
    }

    /**
     * Access method for the Edge's data (such as weight).
     * @return the Object representing the data.
     */
    public D getData() {
        return data;
    }

    /**
     * Access method for the Edge's data (such as weight).
     * @param data the new data for the edge.
     */
    public void setData(D data) {
        this.data = data;
    }
    
    /**
     * Access method for the Edge's undirectedness parameter.
     * @return the boolean indicating if this edge is undirected.
     */
    public boolean isUndirected() {
        return undirected;
    }
    
    /**
     * Access method for the Edge's undirectedness parameter.
     * @param undirected a boolean indicating if this edge should be undirected.
     */
    public void setUndirected(boolean undirected) {
        this.undirected = undirected;
    }

    /**
     * Attempts to duplicate an Edge based on the supplied parameters.
     * The data attached to the edge REMAINS THE SAME OBJECT as the
     * original graph.  As such, non-immutable objects will reflect changes
     * across both edges. The supplied Node objects must be equivalent to
     * the old ones.
     *
     * @param source the new Source node reference to utilize.
     * @param destination the new Destination node reference to utilize.
     * @return a duplicate Edge decoupled from the original Graph.
     */
    public Edge duplicate(Node source, Node destination) {
        if (!source.equals(getSource()) || !destination.equals(getDestination())) {
            throw new IllegalArgumentException("The new source and " + "destination nodes must be equivalent to the old " + "ones!");
        }
        return new Edge(source, destination, data, undirected);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Edge) {
            Edge edge = (Edge) o;
            boolean equivalent = edge.source.equals(source) && edge.destination.equals(destination) ||
                    edge.source.equals(destination) && edge.destination.equals(source) && edge.isUndirected();
            return equivalent;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.source);
        hash = 53 * hash + Objects.hashCode(this.destination);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]:%s", source, destination,
                undirected ? "U" : "D");
    }
    
}
