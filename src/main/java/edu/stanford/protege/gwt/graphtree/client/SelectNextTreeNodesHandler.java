package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.gwt.view.client.SetSelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SelectNextTreeNodesHandler<U extends Serializable> implements TreeNodeViewActionHandler<U> {

    private SetSelectionModel<TreeNode<U>> selectionModel;

    private TreeNodeViewTraverser<U> viewTraverser;

    public SelectNextTreeNodesHandler(SetSelectionModel<TreeNode<U>> selectionModel) {
        this(selectionModel, new TreeNodeViewTraverser<U>());
    }

    public SelectNextTreeNodesHandler(SetSelectionModel<TreeNode<U>> selectionModel, TreeNodeViewTraverser<U> viewTraverser) {
        this.selectionModel = selectionModel;
        this.viewTraverser = viewTraverser;
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> treeNodeViews) {
        for (TreeNodeView<U> view : treeNodeViews) {
            Optional<TreeNodeView<U>> next = viewTraverser.getNext(view);
            if (next.isPresent()) {
                selectionModel.setSelected(next.get().getNode(), true);
                selectionModel.setSelected(view.getNode(), false);
                next.get().scrollIntoView();
            }
        }
    }
}
