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

    private final SelectionModel selectionModel;

    private final TreeNodeViewTraverser<U> viewTraverser;

    public SelectNextTreeNodesHandler(SelectionModel selectionModel) {
        this(selectionModel, new TreeNodeViewTraverser<>());
    }

    public SelectNextTreeNodesHandler(SelectionModel selectionModel, TreeNodeViewTraverser<U> viewTraverser) {
        this.selectionModel = selectionModel;
        this.viewTraverser = viewTraverser;
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> treeNodeViews) {
        for (TreeNodeView<U> view : treeNodeViews) {
            Optional<TreeNodeView<U>> next = viewTraverser.getNext(view);
            next.ifPresent(nextView -> {
                if (!event.isShiftDown()) {
                    selectionModel.setSelected(nextView.getNodeId());
                }
                else {
                    selectionModel.extendSelection(nextView.getNode().getId());
                }
                nextView.scrollIntoView();
            });
        }
    }
}
