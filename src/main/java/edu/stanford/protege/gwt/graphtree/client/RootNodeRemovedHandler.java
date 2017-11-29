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

    private TreeNodeViewManager<U> viewManager;

    private HasWidgets rootNodeContainer;

    public RootNodeRemovedHandler(TreeNodeViewManager<U> viewManager, HasWidgets rootNodeContainer) {
        this.viewManager = viewManager;
        this.rootNodeContainer = rootNodeContainer;
    }

    public void handleRootNodeRemoved(RootNodeRemoved<U> rootNodeRemoved) {
        Optional<TreeNodeView<U>> childView = viewManager.getViewIfPresent(rootNodeRemoved.getRootNode());
        if (childView.isPresent()) {
            TreeNodeView<U> node = childView.get();
            viewManager.releaseView(node.getNodeId());
            rootNodeContainer.remove(node.asWidget());
        }
    }
}
