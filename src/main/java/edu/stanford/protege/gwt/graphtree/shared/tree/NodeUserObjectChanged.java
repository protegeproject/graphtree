package edu.stanford.protege.gwt.graphtree.shared.tree;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class NodeUserObjectChanged<U extends Serializable> extends TreeNodeModelChange<U> {

    private TreeNodeId treeNode;

    private U userObject;

    private NodeUserObjectChanged() {
    }

    public NodeUserObjectChanged(@Nonnull TreeNodeId treeNode,
                                 @Nonnull U userObject) {
        this.treeNode = checkNotNull(treeNode);
        this.userObject = checkNotNull(userObject);
    }

    @Nonnull
    public TreeNodeId getTreeNodeId() {
        return treeNode;
    }

    @Nonnull
    public U getUserObject() {
        return userObject;
    }

    @Override
    public void accept(TreeNodeModelChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "NodeUserObjectChanged".hashCode() + treeNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof NodeUserObjectChanged)) {
            return false;
        }
        NodeUserObjectChanged other = (NodeUserObjectChanged) o;
        return this.treeNode.equals(other.treeNode);
    }

    @Override
    public String toString() {
        return toStringHelper("NodeUserObjectChanged").addValue(treeNode).toString();
    }
}
