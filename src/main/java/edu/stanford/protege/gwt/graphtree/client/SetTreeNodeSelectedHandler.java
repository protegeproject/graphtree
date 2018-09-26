package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.view.client.SetSelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SetTreeNodeSelectedHandler<U extends Serializable> implements TreeNodeViewActionHandler<U> {

    private final SelectionModel selectionModel;

    private final Platform platform;

    private final TreeNodeViewManager<U> viewManager;

    @Inject
    public SetTreeNodeSelectedHandler(@Nonnull SelectionModel selectionModel,
                                      @Nonnull TreeNodeViewManager<U> viewManager,
                                      @Nonnull Platform platform) {
        this.selectionModel = checkNotNull(selectionModel);
        this.platform = checkNotNull(platform);
        this.viewManager = checkNotNull(viewManager);
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> views) {
        for (TreeNodeView<U> view : views) {
            if(isSelectionToggle(event)) {
                toggleSelectionForView(view);
            }
            else if(isSelectionExtend(event)) {
                // Extend the selection.  This means that everything between the first
                // selected item and this view becomes selected
                Optional<TreeNodeView<U>> firstView = getFirstSelectedView();
                firstView.ifPresent(v -> {
                    TreeNodeViewTraverser<U> traverser = TreeNodeViewTraverser.newTreeNodeViewTraverser();
                    List<TreeNodeView<U>> viewList = traverser.getVisibleViewsBetween(v, view);
                    selectionModel.setSelected(viewList.stream().map(TreeNodeView::getNodeId));
                });
            }
            else if(!isContextMenuClick(event, view)) {
                // Single selection
                selectionModel.setSelected(view.getNode().getId());
            }
        }
    }

    private Optional<TreeNodeView<U>> getFirstSelectedView() {
        return selectionModel.getSelection()
                .stream()
                .findFirst()
                .flatMap(viewManager::getViewIfPresent);
    }

    private boolean isSelectionToggle(TreeViewInputEvent<U> event) {
        if(!event.isLeftButton()) {
            return false;
        }
        if(platform.isMacOS()) {
            return event.isMetaDown();
        }
        else {
            return event.isCtrlDown();
        }
    }

    private boolean isSelectionExtend(TreeViewInputEvent<U> event) {
       return event.isShiftDown();
    }

    private boolean isContextMenuClick(TreeViewInputEvent<U> event, TreeNodeView<U> view) {
        return selectionModel.isSelected(view.getNodeId())
                && (platform.isMacOS() && event.isCtrlDown() || !event.isLeftButton());
    }

    private void toggleSelectionForView(TreeNodeView<U> view) {
        selectionModel.toggleSelection(view.getNode().getId());
    }
}
