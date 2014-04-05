package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class AddKeyNode<U extends Serializable> extends GraphModelChange<U> {

    private GraphNode<U> keyNode;

    private AddKeyNode() {
    }

    public AddKeyNode(GraphNode<U> keyNode) {
        this.keyNode = keyNode;
    }

    public GraphNode<U> getNode() {
        return keyNode;
    }

    @Override
    public void accept(GraphModelChangeVisitor<U> visitor) {
        visitor.visit(this);
    }

    @Override
    public RemoveKeyNode<U> getReverseChange() {
        return new RemoveKeyNode<U>(keyNode);
    }

    @Override
    public int hashCode() {
        return "AddKeyNode".hashCode() + keyNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof AddKeyNode)) {
            return false;
        }
        AddKeyNode other = (AddKeyNode) o;
        return this.keyNode.equals(other.keyNode);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("AddKeyNode").addValue(keyNode).toString();
    }
}
