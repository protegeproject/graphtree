package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.tree.*;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.gwt.graphtree.client.TreeNodeViewState.EXPANDED;

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

    private final RootNodeAddedHandler<U> rootNodeAddedHandler;

    private final RootNodeRemovedHandler<U> rootNodeRemovedHandler;

    private final ChildNodeAddedHandler<U> childNodeAddedHandler;

    private final ChildNodeRemovedHandler<U> childNodeRemovedHandler;

    private final NodeRenderingChangedHandler<U> nodeRenderingChangedHandler;

    private TreeNodeModel<U> model = new NullTreeNodeModel<>();

    private HandlerRegistration modelHandlerRegistration;

    @Inject
    public TreePresenter(TreeView treeView, SetSelectionModel<TreeNode<U>> selectionModel, TreeNodeRenderer<U> treeNodeRenderer) {
        this.treeView = treeView;
        this.selectionModel = selectionModel;
        this.viewManager = new TreeNodeViewManager<>(treeNodeRenderer);
        this.pendingChangeManager = new PendingChangesManager<>(this, selectionModel);
        treeNodeViewSelectionProvider = new TreeNodeViewSelectionProvider<>(selectionModel, viewManager);
        KeyboardEventMapper keyboardEventMapper = new KeyboardEventMapper<>(treeNodeViewSelectionProvider,
                                                                            new SetTreeNodeExpandedHandler<>(this, selectionModel, viewManager),
                                                                            new SetTreeNodeCollapsedHandler<>(),
                                                                            new SelectNextTreeNodesHandler<>(selectionModel),
                                                                            new SelectPreviousTreeNodesHandler<>(selectionModel));
        keyboardEventMapper.bind(treeView);
        TreeViewEventTargetFinder<U> eventTargetFinder = new TreeViewEventTargetFinder<>(viewManager);
        MouseEventMapper mouseEventMapper = new MouseEventMapper<>(new SetTreeNodeSelectedHandler<>(selectionModel),
                                                                   new ToggleExpansionStateHandler<>(this, selectionModel, viewManager),
                                                                   eventTargetFinder);
        mouseEventMapper.bind(treeView);
        dragAndDropManager = new DragAndDropEventMapper<>(eventTargetFinder, new TreeNodeViewDragAndDropHandler<>(this));
        dragAndDropManager.bind(treeView);
        SelectionPainter<U> selectionPainter = new SelectionPainter<>(viewManager);
        selectionPainter.bind(selectionModel);
        rootNodeAddedHandler = new RootNodeAddedHandler<>(viewManager, treeView);
        rootNodeRemovedHandler = new RootNodeRemovedHandler<>(viewManager, treeView);
        childNodeAddedHandler = new ChildNodeAddedHandler<>(viewManager);
        childNodeRemovedHandler = new ChildNodeRemovedHandler<>(viewManager);
        nodeRenderingChangedHandler = new NodeRenderingChangedHandler<>(viewManager);
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

    public void getTreeNodesForUserObject(U userObject, GetTreeNodesCallback<U> callback) {
        model.getTreeNodesForUserObject(userObject, callback);
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
        modelHandlerRegistration = model.addTreeNodeModelEventHandler(TreePresenter.this::handleTreeNodeModelEvent);
        reload();
    }

    public void setDropHandler(TreeNodeDropHandler<U> dropHandler) {
        dragAndDropManager.setDropHandler(dropHandler);
    }

    public HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler) {
        return selectionModel.addSelectionChangeHandler(handler);
    }

    public Path<TreeNodeId> getPathToRoot(TreeNodeId fromNode) {
        java.util.Optional<TreeNodeView<U>> view = viewManager.getViewIfPresent(fromNode);
        if (!view.isPresent()) {
            return Path.emptyPath();
        }
        TreeNodeViewTraverser<U> traverser = TreeNodeViewTraverser.newTreeNodeViewTraverser();
        return traverser.getTreeNodePathToRoot(view.get());
    }

    @Override
    public void setTreeNodeExpanded(TreeNodeId node) {
        Optional<TreeNodeView<U>> view = viewManager.getViewIfPresent(node);
        view.ifPresent(v -> setTreeNodeHandleState(TreeViewInputEvent.empty(), v, EXPANDED));
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
        model.getTreeNodesForUserObject(userObject, nodes -> {
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
                if(revealMode == RevealMode.REVEAL_FIRST) {
                    break;
                }
            }
        });
    }

    @Override
    public void getNodes(Optional<TreeNodeId> parentNode, GetTreeNodesCallback<U> callback) {
        model.getNodes(parentNode, callback);
    }

    private void initialiseRootNodes() {
        treeView.clear();
        model.getNodes(Optional.empty(), nodes -> {
            for (TreeNodeData<U> node : nodes) {
                TreeNodeView rootNodeView = viewManager.getView(node);
                treeView.add(rootNodeView.asWidget());
                setTreeNodeExpanded(node.getId());
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

    private void handleTreeNodeModelChange(TreeNodeModelChange<U> change) {

        change.accept(new TreeNodeModelChangeVisitor<U>() {
            public void visit(RootNodeAdded<U> rootNodeAdded) {
                rootNodeAddedHandler.handleRootNodeAdded(rootNodeAdded);
            }

            public void visit(RootNodeRemoved<U> rootNodeRemoved) {
                rootNodeRemovedHandler.handleRootNodeRemoved(rootNodeRemoved);
            }

            public void visit(final ChildNodeAdded<U> childNodeAdded) {
                childNodeAddedHandler.handleChildNodeAdded(childNodeAdded);
            }

            public void visit(ChildNodeRemoved<U> childNodeRemoved) {
                childNodeRemovedHandler.handleChildNodeRemoved(childNodeRemoved);
            }

            public void visit(NodeRenderingChanged<U> nodeRenderingChanged) {
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
            new SetTreeNodeExpandedHandler<>(model, selectionModel, viewManager).invoke(event,
                                                                                        Collections.singleton(treeNodeView));
        }
    }
}
