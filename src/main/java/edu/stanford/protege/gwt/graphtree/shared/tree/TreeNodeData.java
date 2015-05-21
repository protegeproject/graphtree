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
public class TreeNodeData<U extends Serializable> implements Serializable, IsSerializable {

    private TreeNode<U> treeNode;

//    private String htmlRendering;

    private String shortForm;

    private boolean leaf;

    private TreeNodeData() {
    }

    public TreeNodeData(TreeNode<U> treeNode, String shortForm, boolean leaf) {
        this.treeNode = treeNode;
        this.shortForm = shortForm;
        this.leaf = leaf;
    }

    public TreeNodeId getId() {
        return treeNode.getId();
    }

    public TreeNode<U> getTreeNode() {
        return treeNode;
    }

    public U getUserObject() {
        return treeNode.getUserObject();
    }

//    public String getHtmlRendering() {
//        return htmlRendering;
//    }

    public String getShortForm() {
        return shortForm;
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
        return Objects.toStringHelper("TreeNodeData")
                .addValue(treeNode)
                .add("shortForm", shortForm)
//                .add("rendering", htmlRendering)
                .add("leaf", leaf)
                .toString();
    }
}
