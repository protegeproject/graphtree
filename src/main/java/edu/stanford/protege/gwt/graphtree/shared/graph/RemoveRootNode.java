package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class RemoveRootNode<U extends Serializable> extends GraphModelChange<U> {

    private GraphNode<U> rootNode;

    private RemoveRootNode() {
    }

    public RemoveRootNode(GraphNode<U> rootNode) {
        this.rootNode = rootNode;
    }

    public AddRootNode<U> getReverseChange() {
        return new AddRootNode<>(rootNode);
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

    @Override
    public int hashCode() {
        return "RemoveRootNode".hashCode() + rootNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof RemoveRootNode)) {
            return false;
        }
        RemoveRootNode other = (RemoveRootNode) o;
        return this.rootNode.equals(other.rootNode);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("RemoveRootNode").addValue(rootNode).toString();
    }
}
