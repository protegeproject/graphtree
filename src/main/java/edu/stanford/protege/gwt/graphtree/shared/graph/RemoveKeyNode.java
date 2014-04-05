package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class RemoveKeyNode<U extends Serializable> extends GraphModelChange<U> {

    private GraphNode<U> keyNode;

    private RemoveKeyNode() {
    }

    public RemoveKeyNode(GraphNode<U> keyNode) {
        this.keyNode = keyNode;
    }

    @Override
    public AddKeyNode<U> getReverseChange() {
        return new AddKeyNode<U>(keyNode);
    }

    public GraphNode<U> getNode() {
        return keyNode;
    }

    @Override
    public void accept(GraphModelChangeVisitor<U> visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "RemoveKeyNode".hashCode() + keyNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof RemoveKeyNode)) {
            return false;
        }
        RemoveKeyNode other = (RemoveKeyNode) o;
        return this.keyNode.equals(other.keyNode);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("RemoveKeyNode").addValue(keyNode).toString();
    }
}
