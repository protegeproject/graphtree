package edu.stanford.protege.gwt.graphtree.shared.tree;

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
public class NodeRenderingChanged<U extends Serializable> extends TreeNodeModelChange<U> {

    private String htmlRendering;

    private TreeNodeId treeNode;

    private NodeRenderingChanged() {
    }

    public NodeRenderingChanged(TreeNodeId treeNode, String htmlRendering) {
        this.treeNode = treeNode;
        this.htmlRendering = htmlRendering;
    }

    public String getHtmlRendering() {
        return htmlRendering;
    }

    public TreeNodeId getTreeNode() {
        return treeNode;
    }

//    @Override
//    public Set<TreeNode<U>> getTreeNodes() {
//        return Collections.singleton(treeNode);
//    }

    @Override
    public void accept(TreeNodeModelChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "NodeRenderingChanged".hashCode() + treeNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof NodeRenderingChanged)) {
            return false;
        }
        NodeRenderingChanged other = (NodeRenderingChanged) o;
        return this.treeNode.equals(other.treeNode);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("NodeRenderingChanged").addValue(treeNode).addValue(htmlRendering).toString();
    }
}
