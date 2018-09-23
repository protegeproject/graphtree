package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.view.client.SetSelectionModel;
import javax.inject.Inject;

import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;

import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SelectPreviousTreeNodesHandler<U extends Serializable> implements TreeNodeViewActionHandler<U> {

    private final SetSelectionModel<TreeNode<U>> selectionModel;

    private final TreeNodeViewTraverser<U> viewTraverser;

    public SelectPreviousTreeNodesHandler(SetSelectionModel<TreeNode<U>> selectionModel) {
        this(selectionModel, TreeNodeViewTraverser.newTreeNodeViewTraverser());
    }

    @Inject
    public SelectPreviousTreeNodesHandler(SetSelectionModel<TreeNode<U>> selectionModel, TreeNodeViewTraverser<U> viewTraverser) {
        this.selectionModel = selectionModel;
        this.viewTraverser = viewTraverser;
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> views) {
        for (TreeNodeView<U> view : views) {
            Optional<TreeNodeView<U>> previous = viewTraverser.getPrevious(view);
            previous.ifPresent(v -> {
                if (!event.isShiftDown()) {
                    selectionModel.clear();
                }
                selectionModel.setSelected(v.getNode(), true);
                v.scrollIntoView();
            });
        }
    }
}
