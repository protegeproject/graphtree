package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.function.Consumer;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class AddRootNode<U extends Serializable> extends GraphModelChange<U> {

    private GraphNode<U> rootNode;

    private AddRootNode() {
    }

    @JsonCreator
    public AddRootNode(@JsonProperty("node") GraphNode<U> rootNode) {
        this.rootNode = rootNode;
    }

    public GraphNode<U> getNode() {
        return rootNode;
    }

    @Override
    public void accept(GraphModelChangeVisitor<U> visitor) {
        visitor.visit(this);
    }

    @Override
    void forEachGraphNode(Consumer<GraphNode<U>> nodeConsumer) {
        nodeConsumer.accept(rootNode);
    }

    @JsonIgnore
    public RemoveRootNode<U> getReverseChange() {
        return new RemoveRootNode<>(rootNode);
    }

    @Override
    public int hashCode() {
        return "AddRootNode".hashCode() + rootNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof AddRootNode)) {
            return false;
        }
        AddRootNode other = (AddRootNode) o;
        return this.rootNode.equals(other.rootNode);
    }

    @Override
    public String toString() {
        return toStringHelper("AddRootNode").addValue(rootNode).toString();
    }
}
