package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.tree.ChildNodeRemoved;

import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
public class ChildNodeRemovedHandler<U extends Serializable> {

    private final TreeNodeViewManager<U> viewManager;

    public ChildNodeRemovedHandler(TreeNodeViewManager<U> viewManager) {
        this.viewManager = viewManager;
    }

    public void handleChildNodeRemoved(ChildNodeRemoved<U> childNodeRemoved) {
        Optional<TreeNodeView<U>> parentView = viewManager.getViewIfPresent(childNodeRemoved.getParentNode());
        if (!parentView.isPresent()) {
            return;
        }
        Optional<TreeNodeView<U>> childView = viewManager.getViewIfPresent(childNodeRemoved.getChildNode());
        if (!childView.isPresent()) {
            return;
        }
        TreeNodeView<U> parentTreeNodeView = parentView.get();
        parentTreeNodeView.removeChildView(childView.get());
        parentTreeNodeView.setLeaf(parentTreeNodeView.isEmpty());
        viewManager.releaseView(childNodeRemoved.getChildNode());
    }
}
