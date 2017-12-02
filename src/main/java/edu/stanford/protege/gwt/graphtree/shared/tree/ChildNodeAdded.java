package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class ChildNodeAdded<U extends Serializable> extends TreeNodeModelChange<U> {

    private TreeNodeId parentNode;

    private TreeNodeData<U> childNode;

    private ChildNodeAdded() {
    }

    public ChildNodeAdded(TreeNodeId parentNode, TreeNodeData<U> childNode) {
        this.parentNode = parentNode;
        this.childNode = childNode;
    }

    public TreeNodeId getParentNode() {
        return parentNode;
    }

    public TreeNodeData<U> getChildNode() {
        return childNode;
    }

    @Override
    public void accept(TreeNodeModelChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "ChildNodeAdded".hashCode() + parentNode.hashCode() + childNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ChildNodeAdded)) {
            return false;
        }
        ChildNodeAdded other = (ChildNodeAdded) o;
        return this.parentNode.equals(other.parentNode) && this.childNode.equals(other.childNode);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ChildNodeAdded")
                          .add("node", parentNode)
                          .add("childNode", childNode)
                          .toString();
    }
}
