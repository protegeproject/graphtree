package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.tree.*;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class TreePresenter<U extends Serializable> implements HasTreeNodeDropHandler<U>, HasGetNodes<U>, HasPendingChanges<U>, HasSetTreeNodeExpanded {

    private final HasWidgets treeView;

    private final TreeNodeViewManager<U> viewManager;

    private final SetSelectionModel<TreeNode<U>> selectionModel;

    private final DragAndDropEventMapper<U> dragAndDropManager;

    private final PendingChangesManager<U> pendingChangeManager;

    private final TreeNodeViewSelectionProvider<U> treeNodeViewSelectionProvider;

    private final RootNodeAddedHandler rootNodeAddedHandler;

    private final RootNodeRemovedHandler rootNodeRemovedHandler;

    private final ChildNodeAddedHandler childNodeAddedHandler;

    private final ChildNodeRemovedHandler childNodeRemovedHandler;

    private final NodeRenderingChangedHandler nodeRenderingChangedHandler;

    private TreeNodeModel<U> model = new NullTreeNodeModel<U>();

    private HandlerRegistration modelHandlerRegistration;

    @Inject
    public TreePresenter(TreeView treeView, SetSelectionModel<TreeNode<U>> selectionModel, TreeNodeRenderer<U> treeNodeRenderer) {
        this.treeView = treeView;
        this.selectionModel = selectionModel;
        this.viewManager = new TreeNodeViewManager<U>(treeNodeRenderer);
        this.pendingChangeManager = new PendingChangesManager<U>(this, selectionModel);
        treeNodeViewSelectionProvider = new TreeNodeViewSelectionProvider<U>(selectionModel, viewManager);
        KeyboardEventMapper keyboardEventMapper = new KeyboardEventMapper<U>(treeNodeViewSelectionProvider,
                new SetTreeNodeExpandedHandler<U>(this, selectionModel, viewManager),
                new SetTreeNodeCollapsedHandler<U>(),
                new SelectNextTreeNodesHandler<U>(selectionModel),
                new SelectPreviousTreeNodesHandler<U>(selectionModel));
        keyboardEventMapper.bind(treeView);
        TreeViewEventTargetFinder<U> eventTargetFinder = new TreeViewEventTargetFinder<U>(viewManager);
        MouseEventMapper mouseEventMapper = new MouseEventMapper<U>(new SetTreeNodeSelectedHandler<U>(selectionModel),
                new ToggleExpansionStateHandler<U>(this, selectionModel, viewManager),
                eventTargetFinder);
        mouseEventMapper.bind(treeView);
        dragAndDropManager = new DragAndDropEventMapper<U>(eventTargetFinder, new TreeNodeViewDragAndDropHandler<U>(this));
        dragAndDropManager.bind(treeView);
        SelectionPainter<U> selectionPainter = new SelectionPainter<U>(viewManager);
        selectionPainter.bind(selectionModel);
        rootNodeAddedHandler = new RootNodeAddedHandler<U>(viewManager, treeView);
        rootNodeRemovedHandler = new RootNodeRemovedHandler<U>(viewManager, treeView);
        childNodeAddedHandler = new ChildNodeAddedHandler<U>(viewManager);
        childNodeRemovedHandler = new ChildNodeRemovedHandler<U>(viewManager);
        nodeRenderingChangedHandler = new NodeRenderingChangedHandler<U>(viewManager);
    }

    @Override
    public void setChildAdditionPending(TreeNodeView parentView) {
        pendingChangeManager.setChildAdditionPending(parentView);
    }

    @Override
    public void setRemovalPending(TreeNodeView removedView) {
        pendingChangeManager.setRemovalPending(removedView);
    }

    @Override
    public void setRendingChangePending(TreeNodeView view) {
        pendingChangeManager.setRendingChangePending(view);
    }

    @Override
    public void setPendingChangedCancelled(TreeNodeView view) {
        pendingChangeManager.setPendingChangedCancelled(view);
    }


    public void reload() {
        initialiseRootNodes();
    }

    public void setRootNodesExpanded() {
        for(TreeNodeView<U> treeView : getRootViews()) {
            treeView.setExpanded();
        }
    }

    /**
     * Sets the TreeNodeModel used by this presenter.
     * @param model The model.  Not {@code null}.
     * @throws NullPointerException if {@code model} is {@code null}.
     */
    public void setModel(TreeNodeModel<U> model) {
        checkNotNull(model);
        if(modelHandlerRegistration != null) {
            modelHandlerRegistration.removeHandler();
        }
        viewManager.purge();
        this.model = model;
        modelHandlerRegistration = model.addTreeNodeModelEventHandler(new TreeNodeModelEventHandler() {
            public void handleTreeNodeModelEvent(TreeNodeModelEvent event) {
                TreePresenter.this.handleTreeNodeModelEvent(event);
            }
        });
        reload();
    }

    public void setDropHandler(TreeNodeDropHandler<U> dropHandler) {
        dragAndDropManager.setDropHandler(dropHandler);
    }

    public HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler) {
        return selectionModel.addSelectionChangeHandler(handler);
    }

    public Path<TreeNodeId> getPathToRoot(TreeNodeId fromNode) {
        Optional<TreeNodeView<U>> view = viewManager.getViewIfPresent(fromNode);
        if (!view.isPresent()) {
            return Path.emptyPath();
        }
        TreeNodeViewTraverser<U> traverser = TreeNodeViewTraverser.newTreeNodeViewTraverser();
        return traverser.getTreeNodePathToRoot(view.get());
    }

    @Override
    public void setTreeNodeExpanded(TreeNodeId node) {
        Optional<TreeNodeView<U>> view = viewManager.getViewIfPresent(node);
        setTreeNodeHandleState(TreeViewInputEvent.<U>empty(), view.get(), TreeNodeViewState.EXPANDED);
    }

    public void clearSelection() {
        selectionModel.clear();
    }

    public Set<TreeNode<U>> getSelectedSet() {
        return selectionModel.getSelectedSet();
    }

    public boolean isSelected(TreeNode<U> object) {
        return selectionModel.isSelected(object);
    }

    public void setSelected(TreeNode<U> object, boolean selected) {
        selectionModel.setSelected(object, selected);
    }

    public void setSelected(Object key) {
//        Collection<TreeNodeView<U>> views = viewManager.getViewsForKey(key);
//        if(!views.isEmpty()) {
//            TreeNodeView<U> view = views.iterator().next();
//            setSelected(view.getNodeId(), true);
//        }
    }

    public Object getKey(TreeNode<U> item) {
        return selectionModel.getKey(item);
    }

    public void clearPruning() {
        for (TreeNodeView<U> rootView : getRootViews()) {
            TreeNodeViewTraverser<U> traver = TreeNodeViewTraverser
                    .newTreeNodeViewTraverser();
            Iterator<TreeNodeView<U>> iterator = traver.iterator(rootView);
            while (iterator.hasNext()) {
                TreeNodeView itView = iterator.next();
                itView.setHidden(false);
            }
        }
        scrollSelectionIntoView();
    }

    public void scrollSelectionIntoView() {
        for (TreeNodeView<U> selectedView : treeNodeViewSelectionProvider.getSelection()) {
            selectedView.scrollIntoView();
        }
    }

    public void pruneToNodes(Collection<TreeNodeId> treeNodes) {
        final Set<TreeNodeId> tailNodes = Sets.newHashSet(treeNodes);
        final Set<TreeNodeId> pathNodes = Sets.newHashSet();
        for (TreeNodeId treeNode : treeNodes) {
            for (TreeNodeId element : getPathToRoot(treeNode)) {
                pathNodes.add(element);
            }
        }
        Queue<TreeNodeView<U>> queue = Lists.newLinkedList(getRootViews());
        while (!queue.isEmpty()) {
            TreeNodeView<U> view = queue.poll();
            if (!pathNodes.contains(view.getNodeId())) {
                view.setHidden(true);
            }
            else {
                if (!tailNodes.contains(view.getNodeId())) {
                    for (TreeNodeView<U> childView : view.getChildViews()) {
                        queue.add(childView);
                    }
                }
            }
        }
    }

    public void revealTreeNodesForUserObject(final U userObject, final RevealMode revealMode) {
//        model.getBranchesContainingUserObject(userObject, new HasGetBranches.GetBranchesCallback<U>() {
//            @Override
//            public void handleBranches(Multimap<TreeNodeData<U>, TreeNodeData<U>> parent2ChildMap) {
//
//            }
//        });
        model.getTreeNodesForUserObject(userObject, new GetTreeNodesCallback<U>() {
            @Override
            public void handleNodes(List<TreeNodeData<U>> nodes) {
                for(TreeNodeData<U> tn : nodes) {
                    Path<TreeNodeData<U>> pathToRoot = model.getPathToRoot(tn.getId());
                    for(int i = 0; i < pathToRoot.size(); i++) {
                        TreeNodeData<U> treeNodeData = pathToRoot.get(i);
                        if (i < pathToRoot.size() - 1) {
                            viewManager.getView(treeNodeData);
                            setTreeNodeExpanded(treeNodeData.getId());
                        }
                        else {
                            setSelected(tn.getTreeNode(), true);
                        }
                    }
                    if(revealMode == RevealMode.FIRST) {
                        break;
                    }
                }
            }
        });
//        for(TreeNodeData<U> tn : parent2ChildMap.keySet()) {
//            setTreeNodeExpanded(tn.getId());
//        }
    }

    @Override
    public void getNodes(Optional<TreeNodeId> parentNode, GetTreeNodesCallback<U> callback) {
        model.getNodes(parentNode, callback);
    }

    private void initialiseRootNodes() {
        treeView.clear();
        model.getNodes(Optional.<TreeNodeId>absent(), new GetTreeNodesCallback<U>() {
            public void handleNodes(List<TreeNodeData<U>> nodes) {
                for (TreeNodeData<U> node : nodes) {
                    TreeNodeView rootNodeView = viewManager.getView(node);
                    treeView.add(rootNodeView.asWidget());
                    setTreeNodeExpanded(node.getId());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private List<TreeNodeView<U>> getRootViews() {
        List<TreeNodeView<U>> result = Lists.newArrayList();
        for (Widget widget : treeView) {
            if (widget instanceof TreeNodeView) {
                result.add((TreeNodeView<U>) widget);
            }
        }
        return result;
    }

    private void handleTreeNodeModelEvent(TreeNodeModelEvent event) {
        pendingChangeManager.handleTreeNodeModelEvent(event);
        for (TreeNodeModelChange change : event.getChanges()) {
            handleTreeNodeModelChange(change);
        }
    }

    private void handleTreeNodeModelChange(TreeNodeModelChange change) {
        change.accept(new TreeNodeModelChangeVisitor() {
            public void visit(RootNodeAdded rootNodeAdded) {
                rootNodeAddedHandler.handleRootNodeAdded(rootNodeAdded);
            }

            public void visit(RootNodeRemoved rootNodeRemoved) {
                rootNodeRemovedHandler.handleRootNodeRemoved(rootNodeRemoved);
            }

            public void visit(final ChildNodeAdded childNodeAdded) {
                childNodeAddedHandler.handleChildNodeAdded(childNodeAdded);
            }

            public void visit(ChildNodeRemoved childNodeRemoved) {
                childNodeRemovedHandler.handleChildNodeRemoved(childNodeRemoved);
            }

            public void visit(NodeRenderingChanged nodeRenderingChanged) {
                nodeRenderingChangedHandler.handleNodeRenderingChanged(nodeRenderingChanged);
            }
        });
    }

    private void setTreeNodeHandleState(TreeViewInputEvent<U> event,
                                        TreeNodeView<U> treeNodeView,
                                        TreeNodeViewState nextState) {
        if (nextState == TreeNodeViewState.COLLAPSED) {
            new SetTreeNodeCollapsedHandler<U>().invoke(event, Collections.singleton(treeNodeView));
        }
        else {
            new SetTreeNodeExpandedHandler<U>(model, selectionModel, viewManager).invoke(event,
                    Collections.singleton(treeNodeView));
        }
    }
}
