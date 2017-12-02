package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/02/2014
 */
public class TreeNodeData<U extends Serializable> implements Serializable, IsSerializable {

    private TreeNode<U> treeNode;

    private boolean leaf;

    private TreeNodeData() {
    }

    public TreeNodeData(@Nonnull TreeNode<U> treeNode, boolean leaf) {
        this.treeNode = checkNotNull(treeNode);
        this.leaf = leaf;
    }

    @Nonnull
    public TreeNodeId getId() {
        return treeNode.getId();
    }

    @Nonnull
    public TreeNode<U> getTreeNode() {
        return treeNode;
    }

    @Nonnull
    public U getUserObject() {
        return treeNode.getUserObject();
    }

    public boolean isLeaf() {
        return leaf;
    }

    @Override
    public int hashCode() {
        return "TreeNodeData".hashCode() + treeNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof TreeNodeData)) {
            return false;
        }
        TreeNodeData other = (TreeNodeData) o;
        return this.getId().equals(other.getId());
    }

    @Override
    public String toString() {
        return toStringHelper("TreeNodeData")
                          .addValue(treeNode)
                          .add("leaf", leaf)
                          .toString();
    }
}
