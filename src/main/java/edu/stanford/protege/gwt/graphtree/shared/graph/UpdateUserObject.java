package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class UpdateUserObject<U extends Serializable> extends GraphModelChange<U> {

    private U userObject;

    private UpdateUserObject() {
    }

    public UpdateUserObject(@Nonnull U userObject) {
        this.userObject = checkNotNull(userObject);
    }

    @Nonnull
    public U getUserObject() {
        return userObject;
    }

    @Override
    public void accept(GraphModelChangeVisitor<U> visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "UpdateUserObject".hashCode() + userObject.hashCode();
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
        return this.userObject.equals(other.userObject);
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper("UpdateUserObject")
                .addValue(userObject)
                .toString();
    }
}
