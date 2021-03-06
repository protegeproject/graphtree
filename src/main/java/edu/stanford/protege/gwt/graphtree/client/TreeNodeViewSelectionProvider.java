package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.Lists;
import com.google.gwt.view.client.SetSelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2014
 */
public class TreeNodeViewSelectionProvider<U extends Serializable> implements Iterable<TreeNodeView<U>> {

    private final SelectionModel selectionModel;

    private final TreeNodeViewMapper<U> viewMapper;

    public TreeNodeViewSelectionProvider(@Nonnull SelectionModel selectionModel,
                                         @Nonnull TreeNodeViewMapper<U> viewMapper) {
        this.selectionModel = checkNotNull(selectionModel);
        this.viewMapper = checkNotNull(viewMapper);
    }

    @Override
    @Nonnull
    public Iterator<TreeNodeView<U>> iterator() {
        return getSelection().iterator();
    }

    public Iterable<TreeNodeView<U>> getSelection() {
        Set<TreeNodeId> selectedSet = selectionModel.getSelection();
        if(selectedSet.isEmpty()) {
            return Collections.emptyList();
        }
        List<TreeNodeView<U>> result = Lists.newArrayList();
        for(TreeNodeId selectedNodeId : selectedSet) {
            Optional<TreeNodeView<U>> view = viewMapper.getViewIfPresent(selectedNodeId);
            view.ifPresent(result::add);
        }
        return result;
    }
}
