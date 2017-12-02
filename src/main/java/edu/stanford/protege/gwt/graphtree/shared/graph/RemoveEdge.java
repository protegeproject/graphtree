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
public class RemoveEdge<U extends Serializable> extends EdgeChange<U> {

    private RemoveEdge() {
    }

    public RemoveEdge(GraphEdge<U> edge) {
        super(edge);
    }

    public AddEdge<U> getReverseChange() {
        return new AddEdge<U>(getEdge());
    }

    @Override
    public void accept(GraphModelChangeVisitor<U> visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "RemoveEdge".hashCode() + getEdge().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof RemoveEdge)) {
            return false;
        }
        RemoveEdge other = (RemoveEdge) o;
        return this.getEdge().equals(other.getEdge());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("RemoveEdge")
                          .addValue(getEdge())
                          .toString();
    }
}
