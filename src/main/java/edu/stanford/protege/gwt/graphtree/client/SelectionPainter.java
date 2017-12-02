package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.Sets;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.inject.Inject;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SelectionPainter<U extends Serializable> {

    private SetSelectionModel<TreeNode<U>> selectionModel;

    private final TreeNodeViewMapper<U> treeNodeViewManager;

    private final Set<TreeNode<U>> lastSelection = Sets.newHashSet();

    private HandlerRegistration handlerRegistration;

    @Inject
    public SelectionPainter(TreeNodeViewMapper<U> viewMapper) {
        this.treeNodeViewManager = viewMapper;
    }

    private void repaintSelection() {
        if(selectionModel == null) {
            return;
        }
        repaintTreeNodes(lastSelection, false);
        repaintTreeNodes(selectionModel.getSelectedSet(), true);
    }

    private void repaintTreeNodes(Set<TreeNode<U>> nodes, boolean selected) {
        for (TreeNode<U> node : nodes) {
            Optional<TreeNodeView<U>> view = treeNodeViewManager.getViewIfPresent(node.getId());
            view.ifPresent(theView -> theView.setSelected(selected));
        }
    }

    private void handleSelectionChange() {
        if(selectionModel == null) {
            return;
        }
        repaintSelection();
        lastSelection.clear();
        lastSelection.addAll(selectionModel.getSelectedSet());
    }

    public void bind(SetSelectionModel<TreeNode<U>> selectionModel) {
        this.selectionModel = selectionModel;
        handlerRegistration = selectionModel.addSelectionChangeHandler(event -> handleSelectionChange());
    }

    public void unbind() {
        handlerRegistration.removeHandler();
        selectionModel = null;
    }
}
