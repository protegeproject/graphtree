package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.user.client.ui.HasWidgets;
import edu.stanford.protege.gwt.graphtree.shared.tree.RootNodeAdded;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
public class RootNodeAddedHandler<U extends Serializable> {

    private TreeNodeViewManager<U> viewManager;

    private HasWidgets rootNodeContainer;

    public RootNodeAddedHandler(TreeNodeViewManager<U> viewManager, HasWidgets rootNodeContainer) {
        this.viewManager = viewManager;
        this.rootNodeContainer = rootNodeContainer;
    }

    public void handleRootNodeAdded(RootNodeAdded<U> rootNodeAdded) {
        TreeNodeView nodeView = viewManager.getView(rootNodeAdded.getRootNode());
        rootNodeContainer.add(nodeView.asWidget());
    }
}
