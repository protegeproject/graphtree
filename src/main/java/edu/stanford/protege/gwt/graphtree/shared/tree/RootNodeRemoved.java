package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class RootNodeRemoved<U extends Serializable> extends TreeNodeModelChange<U> {

    private TreeNodeId rootNode;

    private RootNodeRemoved() {
    }

    public RootNodeRemoved(TreeNodeId rootNode) {
        this.rootNode = rootNode;
    }

    public TreeNodeId getRootNode() {
        return rootNode;
    }

    @Override
    public void accept(TreeNodeModelChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "RootNodeRemoved".hashCode() + rootNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof RootNodeRemoved)) {
            return false;
        }
        RootNodeRemoved other = (RootNodeRemoved) o;
        return this.rootNode.equals(other.rootNode);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("RootNodeRemoved")
                          .addValue(rootNode)
                          .toString();
    }
}
