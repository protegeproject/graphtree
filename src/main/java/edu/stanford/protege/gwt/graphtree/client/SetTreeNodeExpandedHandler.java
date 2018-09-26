package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.Lists;
import com.google.gwt.view.client.SetSelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SetTreeNodeExpandedHandler<U extends Serializable> implements TreeNodeViewActionHandler<U> {


    private final HasGetNodes<U> hasGetNodes;

    private final TreeNodeViewManager<U> viewManager;

    private final SelectionModel selectionModel;

    public SetTreeNodeExpandedHandler(HasGetNodes<U> hasGetNodes, SelectionModel selectionModel, TreeNodeViewManager<U> viewMapper) {
        this.hasGetNodes = hasGetNodes;
        this.viewManager = viewMapper;
        this.selectionModel = selectionModel;
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> views) {
        for (TreeNodeView<U> view : views) {
            if(view.isExpanded()) {
                return;
            }
            if (view.getDataState() == DataState.UNLOADED) {
                view.setDataState(DataState.LOADING);
                Optional<TreeNodeId> parent = Optional.of(view.getNodeId());
                hasGetNodes.getNodes(parent, new LoadChildNodeViewsCallback<>(view, selectionModel, viewManager));
            }
            else {
                view.setExpanded();
            }
        }
    }

    private static class LoadChildNodeViewsCallback<U extends Serializable> implements GetTreeNodesCallback<U> {

        private final TreeNodeViewManager<U> viewManager;

        private final TreeNodeView<U> parentView;

        private final SelectionModel selectionModel;

        protected LoadChildNodeViewsCallback(TreeNodeView<U> parentView, SelectionModel selectionModel, TreeNodeViewManager<U> viewManager) {
            this.parentView = parentView;
            this.viewManager = viewManager;
            this.selectionModel = selectionModel;
        }

        @Override
        public void handleNodes(List<TreeNodeData<U>> childNodes) {
            addAllChildren(parentView, childNodes);
            parentView.setViewState(TreeNodeViewState.EXPANDED);
        }

        private void addAllChildren(TreeNodeView<U> parentView, List<TreeNodeData<U>> childNodes) {
            List<TreeNodeView<U>> childViews = Lists.newArrayList();
            for (TreeNodeData<U> childNode : childNodes) {
                TreeNodeView<U> childView = createAndSetupChildView(parentView, childNode);
                childViews.add(childView);
            }
            parentView.setChildViews(childViews);
            parentView.setDataState(DataState.LOADED);
            parentView.setLeaf(childViews.isEmpty());
        }

        private TreeNodeView<U> createAndSetupChildView(TreeNodeView<U> parentView, TreeNodeData<U> childNode) {
            final TreeNodeView<U> childView = viewManager.getView(childNode);
            childView.setDepth(parentView.getDepth() + 1);
            childView.setSelected(selectionModel.isSelected(childNode.getTreeNode().getId()));
            return childView;
        }
    }
}
