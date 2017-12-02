package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.view.client.SetSelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;

import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SelectNextTreeNodesHandler<U extends Serializable> implements TreeNodeViewActionHandler<U> {

    private final SetSelectionModel<TreeNode<U>> selectionModel;

    private final TreeNodeViewTraverser<U> viewTraverser;

    public SelectNextTreeNodesHandler(SetSelectionModel<TreeNode<U>> selectionModel) {
        this(selectionModel, new TreeNodeViewTraverser<>());
    }

    public SelectNextTreeNodesHandler(SetSelectionModel<TreeNode<U>> selectionModel, TreeNodeViewTraverser<U> viewTraverser) {
        this.selectionModel = selectionModel;
        this.viewTraverser = viewTraverser;
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> treeNodeViews) {
        for (TreeNodeView<U> view : treeNodeViews) {
            Optional<TreeNodeView<U>> next = viewTraverser.getNext(view);
            next.ifPresent(nextView -> {
                selectionModel.setSelected(nextView.getNode(), true);
                selectionModel.setSelected(view.getNode(), false);
                nextView.scrollIntoView();
            });
        }
    }
}
