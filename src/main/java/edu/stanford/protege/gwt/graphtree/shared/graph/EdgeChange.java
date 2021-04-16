package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/01/2014
 */
@JsonSubTypes({
        @Type(AddEdge.class),
        @Type(RemoveEdge.class)
})
public abstract class EdgeChange<U extends Serializable> extends GraphModelChange<U> {

    private GraphEdge<U> edge;

    protected EdgeChange() {

    }

    protected EdgeChange(GraphEdge<U> edge) {
        this.edge = edge;
    }

    @JsonIgnore
    public GraphNode<U> getPredecessor() {
        return edge.getPredecessor();
    }

    @JsonIgnore
    public GraphNode<U> getSuccessor() {
        return edge.getSuccessor();
    }

    public GraphEdge<U> getEdge() {
        return edge;
    }

    @Override
    void forEachGraphNode(Consumer<GraphNode<U>> nodeConsumer) {
        nodeConsumer.accept(edge.getPredecessor());
        nodeConsumer.accept(edge.getSuccessor());
    }
}
