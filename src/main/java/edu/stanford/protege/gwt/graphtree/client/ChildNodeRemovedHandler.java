package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.core.client.GWT;
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
        parentView.ifPresent(theParentView -> {
            Optional<TreeNodeView<U>> childView = viewManager.getViewIfPresent(childNodeRemoved.getChildNode());
            childView.ifPresent(theChildView -> {
                theParentView.removeChildView(theChildView, () -> {
                    theParentView.setLeaf(theParentView.isEmpty());
                    viewManager.releaseView(childNodeRemoved.getChildNode());
                });
            });
        });
    }
}
