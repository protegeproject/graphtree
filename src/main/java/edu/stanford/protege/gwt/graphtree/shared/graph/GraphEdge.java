package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/01/2014
 */
public class GraphEdge<U extends Serializable> implements Serializable {

    private GraphNode<U> predecessor;

    private GraphNode<U> successor;

    private GraphEdge() {
    }

    public GraphEdge(GraphNode<U> predecessor, GraphNode<U> successor) {
        this.predecessor = predecessor;
        this.successor = successor;
    }

    public GraphNode<U> getPredecessor() {
        return predecessor;
    }

    public GraphNode<U> getSuccessor() {
        return successor;
    }

    @Override
    public int hashCode() {
        return "GraphEdge".hashCode() + predecessor.hashCode() + 13 * successor.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof GraphEdge)) {
            return false;
        }
        GraphEdge other = (GraphEdge) o;
        return this.predecessor.equals(other.predecessor) && this.successor.equals(other.getSuccessor());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Edge")
                          .add("predecessor", predecessor)
                          .add("successor", successor)
                          .toString();
    }
}
