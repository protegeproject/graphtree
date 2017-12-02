package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class RootNodeAdded<U extends Serializable> extends TreeNodeModelChange<U> {

    private TreeNodeData<U> rootNode;

    private RootNodeAdded() {
    }

    public RootNodeAdded(TreeNodeData<U> rootNode) {
        this.rootNode = rootNode;
    }

    public TreeNodeData<U> getRootNode() {
        return rootNode;
    }

//    @Override
//    public Set<TreeNode<U>> getTreeNodes() {
//        return Collections.singleton(rootNode);
//    }

    @Override
    public void accept(TreeNodeModelChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "RootNodeAdded".hashCode() + rootNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof RootNodeAdded)) {
            return false;
        }
        RootNodeAdded other = (RootNodeAdded) o;
        return this.rootNode.equals(other.rootNode);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("RootNodeAdded")
                          .addValue(rootNode)
                          .toString();
    }
}
