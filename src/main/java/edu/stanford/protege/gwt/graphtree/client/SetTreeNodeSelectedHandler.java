package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.view.client.SetSelectionModel;
import com.google.inject.Inject;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SetTreeNodeSelectedHandler<U extends Serializable> implements TreeNodeViewActionHandler<U> {

    private final SetSelectionModel<TreeNode<U>> selectionModel;

    @Inject
    public SetTreeNodeSelectedHandler(SetSelectionModel<TreeNode<U>> selectionModel) {
        this.selectionModel = selectionModel;
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> views) {
        for (TreeNodeView<U> view : views) {
            if(event.isAltDown()) {
                // Toggle selection
                selectionModel.setSelected(view.getNode(), !selectionModel.isSelected(view.getNode()));
            }
            else {
                // Single selection
                selectionModel.clear();
                selectionModel.setSelected(view.getNode(), true);
            }
        }
    }
}
