package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import edu.stanford.protege.gwt.graphtree.shared.tree.ChildNodeAdded;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 * <p>
 *     Processed {@link edu.stanford.protege.gwt.graphtree.shared.tree.ChildNodeAdded} events.  When NodeA is added
 *     as a child of NodeB this ensures that if there is a view NodeBView for NodeB then there is a child view,
 *     NodeAChild of NodeBView.  It also ensures that NodeAView is set up appropriately by setting its depth, and
 *     ensures that NodeBView is not shown as a leaf view.
 * </p>
 */
public class ChildNodeAddedHandler<U extends Serializable> {

    private TreeNodeViewManager<U> viewManager;

    @Inject
    public ChildNodeAddedHandler(TreeNodeViewManager<U> viewManager) {
        this.viewManager = viewManager;
    }

    public void handleChildNodeAdded(ChildNodeAdded<U> childNodeAdded) {
        TreeNodeId parentNode = childNodeAdded.getParentNode();
        final Optional<TreeNodeView<U>> pv = viewManager.getViewIfPresent(parentNode);
        if (!pv.isPresent()) {
            return;
        }
        TreeNodeView<U> parentView = pv.get();
        TreeNodeView<U> childView = viewManager.getView(childNodeAdded.getChildNode());
        childView.setDepth(parentView.getDepth() + 1);
        parentView.setLeaf(false);
        parentView.addChildView(childView);
    }
}
