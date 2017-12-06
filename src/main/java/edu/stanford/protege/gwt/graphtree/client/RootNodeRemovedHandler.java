package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.user.client.ui.HasWidgets;
import edu.stanford.protege.gwt.graphtree.shared.tree.RootNodeRemoved;

import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
public class RootNodeRemovedHandler<U extends Serializable> {

    private final TreeNodeViewManager<U> viewManager;

    private final TreeView<U> rootNodeContainer;

    public RootNodeRemovedHandler(TreeNodeViewManager<U> viewManager, TreeView<U> rootNodeContainer) {
        this.viewManager = viewManager;
        this.rootNodeContainer = rootNodeContainer;
    }

    public void handleRootNodeRemoved(RootNodeRemoved<U> rootNodeRemoved) {
        Optional<TreeNodeView<U>> childView = viewManager.getViewIfPresent(rootNodeRemoved.getRootNode());
        childView.ifPresent(view -> {
            int viewIndex = rootNodeContainer.getIndexOf(view);
            if(viewIndex != -1) {
                int previousSiblingIndex = viewIndex - 1;
                TreeNodeView<U> previousSibling = null;
                if(previousSiblingIndex >= 0) {
                    previousSibling = rootNodeContainer.getTreeNodeViewAt(previousSiblingIndex);
                }
                int nextSiblingIndex = viewIndex + 1;
                TreeNodeView<U> nextSibling = null;
                if(nextSiblingIndex < rootNodeContainer.getTreeNodeViewCount()) {
                    nextSibling = rootNodeContainer.getTreeNodeViewAt(nextSiblingIndex);
                }
                viewManager.releaseView(view.getNodeId());
                rootNodeContainer.remove(view.asWidget());
                if(previousSibling != null) {
                    if(nextSibling != null) {
                        previousSibling.setNextSibling(nextSibling);
                        nextSibling.setPreviousSibling(previousSibling);
                    }
                    else {
                        previousSibling.setNextSibling(null);
                    }
                }
                else {
                    if(nextSibling != null) {
                        nextSibling.setPreviousSibling(null);
                    }
                }
            }

        });
    }
}
