package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class ChildNodeRemoved<U extends Serializable> extends TreeNodeModelChange {

    private TreeNodeId treeNode;

    private TreeNodeId childNode;

    private ChildNodeRemoved() {
    }

    public ChildNodeRemoved(TreeNodeId treeNode, TreeNodeId childNode) {
        this.treeNode = treeNode;
        this.childNode = childNode;
    }

    public TreeNodeId getParentNode() {
        return treeNode;
    }

    public TreeNodeId getChildNode() {
        return childNode;
    }

//    @Override
//    public Set<TreeNode<U>> getTreeNodes() {
//        return Sets.newHashSet(treeNode, childNode);
//    }

    @Override
    public void accept(TreeNodeModelChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "ChildNodeRemoved".hashCode() + treeNode.hashCode() + childNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ChildNodeRemoved)) {
            return false;
        }
        ChildNodeRemoved other = (ChildNodeRemoved) o;
        return this.treeNode.equals(other.treeNode) && this.childNode.equals(other.childNode);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ChildNodeRemoved")
                          .add("node", treeNode)
                          .add("childNode", childNode)
                          .toString();
    }
}
