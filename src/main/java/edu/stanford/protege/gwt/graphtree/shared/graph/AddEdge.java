package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class AddEdge<U extends Serializable> extends EdgeChange<U> {

    private AddEdge() {
    }

    public AddEdge(GraphEdge<U> edge) {
        super(edge);
    }

    @JsonIgnore
    public RemoveEdge<U> getReverseChange() {
        return new RemoveEdge<>(getEdge());
    }

    @Override
    public void accept(GraphModelChangeVisitor<U> visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "AddEdge".hashCode() + getEdge().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof AddEdge)) {
            return false;
        }
        AddEdge other = (AddEdge) o;
        return this.getEdge().equals(other.getEdge());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("AddEdge")
                          .addValue(getEdge())
                          .toString();
    }
}
