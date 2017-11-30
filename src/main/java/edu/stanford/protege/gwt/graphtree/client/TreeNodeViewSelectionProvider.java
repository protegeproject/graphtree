package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.Lists;
import com.google.gwt.view.client.SetSelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2014
 */
public class TreeNodeViewSelectionProvider<U extends Serializable> implements Iterable<TreeNodeView<U>> {

    private SetSelectionModel<TreeNode<U>> selectionModel;

    private TreeNodeViewMapper<U> viewMapper;

    public TreeNodeViewSelectionProvider(SetSelectionModel<TreeNode<U>> selectionModel, TreeNodeViewMapper<U> viewMapper) {
        this.selectionModel = selectionModel;
        this.viewMapper = viewMapper;
    }

    @Override
    public Iterator<TreeNodeView<U>> iterator() {
        return getSelection().iterator();
    }

    public Iterable<TreeNodeView<U>> getSelection() {
        Set<TreeNode<U>> selectedSet = selectionModel.getSelectedSet();
        if(selectedSet.isEmpty()) {
            return Collections.emptyList();
        }
        List<TreeNodeView<U>> result = Lists.newArrayList();
        for(TreeNode<U> selectedNode : selectedSet) {
            Optional<TreeNodeView<U>> view = viewMapper.getViewIfPresent(selectedNode.getId());
            view.ifPresent(result::add);
        }
        return result;
    }
}
