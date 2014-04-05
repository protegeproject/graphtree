package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.inject.Inject;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SelectPreviousTreeNodesHandler<U extends Serializable> implements TreeNodeViewActionHandler<U> {

    private SetSelectionModel<TreeNode<U>> selectionModel;

    private TreeNodeViewTraverser<U> viewTraverser;

    public SelectPreviousTreeNodesHandler(SetSelectionModel<TreeNode<U>> selectionModel) {
        this(selectionModel, TreeNodeViewTraverser.<U>newTreeNodeViewTraverser());
    }

    @Inject
    public SelectPreviousTreeNodesHandler(SetSelectionModel<TreeNode<U>> selectionModel, TreeNodeViewTraverser<U> viewTraverser) {
        this.selectionModel = selectionModel;
        this.viewTraverser = viewTraverser;
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> views) {
        for (TreeNodeView<U> view : views) {
            Optional<TreeNodeView<U>> previous = viewTraverser.getPrevious(view);
            if (previous.isPresent()) {
                selectionModel.setSelected(previous.get().getNode(), true);
                selectionModel.setSelected(view.getNode(), false);
                previous.get().scrollIntoView();
            }
        }
    }
}
