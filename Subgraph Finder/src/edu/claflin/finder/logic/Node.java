/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.claflin.finder.logic;

import static edu.claflin.finder.Global.getLogger;
import edu.claflin.finder.log.LogLevel;
import java.util.Objects;

/**
 * Represents a node in memory.  A simple implementation requiring only an
 * identifier.
 * 
 * @author Charles Allen Schultz II
 * @version 1.1.1 June 16, 2015
 */
public class Node {
    /**
     * The string representing the node's unique name.
     */
    private final String identifier;

    /**
     * Initializes the Node object.
     *
     * @param identifier the String representing the Node's identifier.
     */
    public Node(String identifier) {
        this.identifier = identifier;
        
        if (getLogger() != null) {
            getLogger().logInfo(LogLevel.DEBUG, "Node: Created Node with "
                    + "identifier: " + identifier);
        }
    }

    /**
     * Access method for the Node's identifier.
     *
     * @return the String identifying the Node.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns a copy of this Node that is a new object in memory.
     * @return
     */
    public Node duplicate() {
        return new Node(identifier);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            Node node = (Node) o;
            return node.getIdentifier().equals(identifier);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.identifier);
        return hash;
    }

    @Override
    public String toString() {
        return getIdentifier();
    }
}
