package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class UpdateUserObject<U extends Serializable> extends GraphModelChange<U> {

    private GraphNode<U> graphNode;

    private UpdateUserObject() {
    }

    public UpdateUserObject(GraphNode<U> graphNode) {
        this.graphNode = graphNode;
    }

    public GraphNode<U> getGraphNode() {
        return graphNode;
    }

    @Override
    public GraphModelChange<U> getReverseChange() {
        return new UpdateUserObject<U>(graphNode);
    }

    @Override
    public void accept(GraphModelChangeVisitor<U> visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "UpdateUserObject".hashCode() + graphNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof UpdateUserObject)) {
            return false;
        }
        UpdateUserObject other = (UpdateUserObject) o;
        return this.graphNode.equals(other.graphNode);
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper("UpdateUserObject")
                .addValue(graphNode)
                .toString();
    }
}
