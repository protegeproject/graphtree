package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/02/2014
 */
public class TreeNodeId implements Serializable, IsSerializable {

    private int id;

    private TreeNodeId() {
    }

    public TreeNodeId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return "TreeNodeId".hashCode() + id;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof TreeNodeId)) {
            return false;
        }
        TreeNodeId other = (TreeNodeId) o;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("TreeNodeId").addValue(id).toString();
    }
}
