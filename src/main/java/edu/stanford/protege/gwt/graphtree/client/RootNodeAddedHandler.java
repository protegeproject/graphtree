package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.protege.gwt.graphtree.shared.tree.RootNodeAdded;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
public class RootNodeAddedHandler<U extends Serializable> {

    private final TreeNodeViewManager<U> viewManager;

    private final TreeView<U> rootNodeContainer;

    public RootNodeAddedHandler(TreeNodeViewManager<U> viewManager, TreeView<U> rootNodeContainer) {
        this.viewManager = viewManager;
        this.rootNodeContainer = rootNodeContainer;
    }

    public void handleRootNodeAdded(RootNodeAdded<U> rootNodeAdded) {
        int rootNodeCount = rootNodeContainer.getTreeNodeViewCount();
        TreeNodeView<U> lastView = null;
        if(rootNodeCount > 0) {
            lastView = rootNodeContainer.getTreeNodeViewAt(rootNodeCount - 1);
        }
        TreeNodeView<U> nodeView = viewManager.getView(rootNodeAdded.getRootNode());
        rootNodeContainer.add(nodeView.asWidget());
        if(lastView != null) {
            lastView.setNextSibling(nodeView);
            nodeView.setPreviousSibling(lastView);
        }
    }
}
