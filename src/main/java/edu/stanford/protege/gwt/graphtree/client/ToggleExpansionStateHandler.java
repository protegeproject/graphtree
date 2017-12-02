package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.view.client.SetSelectionModel;
import com.google.inject.Inject;
import edu.stanford.protege.gwt.graphtree.shared.tree.HasGetNodes;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class ToggleExpansionStateHandler<U extends Serializable> implements TreeNodeViewActionHandler<U> {

    private final SetTreeNodeExpandedHandler<U> setTreeNodeExpandedAction;

    private final SetTreeNodeCollapsedHandler<U> setTreeNodeCollapsedAction;

    public ToggleExpansionStateHandler(HasGetNodes<U> hasGetNodes, SetSelectionModel<TreeNode<U>> selectionModel, TreeNodeViewManager<U> viewManager) {
        this(new SetTreeNodeExpandedHandler<U>(hasGetNodes, selectionModel, viewManager), new SetTreeNodeCollapsedHandler<U>());
    }

    @Inject
    public ToggleExpansionStateHandler(SetTreeNodeExpandedHandler<U> setTreeNodeExpandedAction, SetTreeNodeCollapsedHandler<U> setTreeNodeCollapsedAction) {
        this.setTreeNodeExpandedAction = setTreeNodeExpandedAction;
        this.setTreeNodeCollapsedAction = setTreeNodeCollapsedAction;
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> views) {
        for (TreeNodeView<U> treeNodeView : views) {
            if(treeNodeView.isExpanded()) {
                setTreeNodeCollapsedAction.invoke(event, Collections.singleton(treeNodeView));
            }
            else {
                setTreeNodeExpandedAction.invoke(event, Collections.singleton(treeNodeView));
            }
        }
    }
}
