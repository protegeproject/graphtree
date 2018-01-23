package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.view.client.SetSelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SetTreeNodeSelectedHandler<U extends Serializable> implements TreeNodeViewActionHandler<U> {

    private final SetSelectionModel<TreeNode<U>> selectionModel;

    private final Platform platform;

    @Inject
    public SetTreeNodeSelectedHandler(@Nonnull SetSelectionModel<TreeNode<U>> selectionModel,
                                      @Nonnull Platform platform) {
        this.selectionModel = checkNotNull(selectionModel);
        this.platform = checkNotNull(platform);
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> views) {
        for (TreeNodeView<U> view : views) {
            if(isSelectionToggle(event)) {
                toggleSelectionForView(view);
            }
            else {
                // Single selection
                selectionModel.clear();
                selectionModel.setSelected(view.getNode(), true);
            }
        }
    }

    private boolean isSelectionToggle(TreeViewInputEvent<U> event) {
        if(platform.isMacOS()) {
            return event.isMetaDown();
        }
        else {
            return event.isCtrlDown();
        }
    }

    private void toggleSelectionForView(TreeNodeView<U> view) {
        selectionModel.setSelected(view.getNode(), !selectionModel.isSelected(view.getNode()));
    }
}
