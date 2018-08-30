package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.Sets;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.SetSelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SelectionPainter<U extends Serializable> {

    private final TreeNodeViewMapper<U> treeNodeViewManager;

    private final Set<TreeNodeId> lastSelection = Sets.newHashSet();

    private SetSelectionModel<TreeNode<U>> selectionModel;

    private HandlerRegistration handlerRegistration;

    @Inject
    public SelectionPainter(TreeNodeViewMapper<U> viewMapper) {
        this.treeNodeViewManager = viewMapper;
    }

    private void repaintSelection() {
        if (selectionModel == null) {
            return;
        }
        repaintTreeNodes(lastSelection.stream(), false);
        repaintTreeNodes(selectionModel.getSelectedSet().stream().map(TreeNode::getId), true);
    }

    private void repaintTreeNodes(Stream<TreeNodeId> nodeIds, boolean selected) {
        nodeIds.map(treeNodeViewManager::getViewIfPresent)
               .forEach(view -> view.ifPresent(
                       theView -> theView.setSelected(selected)
               ));
    }

    private void handleSelectionChange() {
        if (selectionModel == null) {
            return;
        }
        repaintSelection();
        lastSelection.clear();
        selectionModel.getSelectedSet().stream()
                      .map(TreeNode::getId)
                      .forEach(lastSelection::add);
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
