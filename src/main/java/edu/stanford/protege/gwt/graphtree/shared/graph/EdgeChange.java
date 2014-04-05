package edu.stanford.protege.gwt.graphtree.shared.graph;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/01/2014
 */
public abstract class EdgeChange<U extends Serializable> extends GraphModelChange<U> {

    private GraphEdge<U> edge;

    protected EdgeChange() {

    }

    protected EdgeChange(GraphEdge<U> edge) {
        this.edge = edge;
    }

    public GraphNode<U> getPredecessor() {
        return edge.getPredecessor();
    }

    public GraphNode<U> getSuccessor() {
        return edge.getSuccessor();
    }

    public GraphEdge<U> getEdge() {
        return edge;
    }
}
