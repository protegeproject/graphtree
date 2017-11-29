package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2013
 */
public class TreeNode<U extends Serializable> implements Serializable, IsSerializable {

    private int id;

    private U userObject;

    private TreeNode() {
    }

    public TreeNode(@Nonnull TreeNodeId id, @Nonnull U userObject) {
        this.id = checkNotNull(id).getId();
        this.userObject = checkNotNull(userObject);
    }

    @Nonnull
    public TreeNodeId getId() {
        return new TreeNodeId(id);
    }

    public U getUserObject() {
        return userObject;
    }

    @Override
    public int hashCode() {
        return "TreeNode".hashCode() + id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof TreeNode)) {
            return false;
        }
        TreeNode other = (TreeNode) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return toStringHelper("TreeNode")
                .add("id", id)
                .addValue(userObject)
                .toString();
    }
}
