package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.tree.*;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.gwt.graphtree.client.TreeNodeViewState.EXPANDED;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 21/01/2014
 */
public class TreePresenter<U extends Serializable, K> implements HasTreeNodeDropHandler<U>, HasGetNodes<U>, HasPendingChanges<U>, HasSetTreeNodeExpanded {

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

    private NodeUserObjectChangedHandler<U> nodeUserObjectChangedHandler;

    private TreeNodeModel<U, K> model = new NullTreeNodeModel<>();

    private HandlerRegistration modelHandlerRegistration;
    private final KeyboardEventMapper keyboardEventMapper;

    @Inject
    public TreePresenter(@Nonnull TreeView<U> treeView,
                         @Nonnull SetSelectionModel<TreeNode<U>> selectionModel,
                         @Nonnull TreeNodeRenderer<U> treeNodeRenderer) {
        this.treeView = checkNotNull(treeView);
        this.selectionModel = checkNotNull(selectionModel);
        this.viewManager = new TreeNodeViewManager<>(checkNotNull(treeNodeRenderer));
        this.pendingChangeManager = new PendingChangesManager<>(this, selectionModel);
        treeNodeViewSelectionProvider = new TreeNodeViewSelectionProvider<>(selectionModel, viewManager);
        keyboardEventMapper = new KeyboardEventMapper<>(treeNodeViewSelectionProvider,
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
        dragAndDropManager = new DragAndDropEventMapper<>(treeView, eventTargetFinder, new TreeNodeViewDragAndDropHandler<>(this));
        SelectionPainter<U> selectionPainter = new SelectionPainter<>(viewManager);
        selectionPainter.bind(selectionModel);
        rootNodeAddedHandler = new RootNodeAddedHandler<>(viewManager, treeView);
        rootNodeRemovedHandler = new RootNodeRemovedHandler<>(viewManager, treeView);
        childNodeAddedHandler = new ChildNodeAddedHandler<>(viewManager);
        childNodeRemovedHandler = new ChildNodeRemovedHandler<>(viewManager);
        nodeUserObjectChangedHandler = new NodeUserObjectChangedHandler<>(viewManager);
    }


    /**
     * Sets the TreeNodeModel used by this presenter.
     *
     * @param model The model.  Not {@code null}.
     * @throws NullPointerException if {@code model} is {@code null}.
     */
    public void setModel(@Nonnull TreeNodeModel<U, K> model) {
        checkNotNull(model);
        if (modelHandlerRegistration != null) {
            modelHandlerRegistration.removeHandler();
        }
        viewManager.purge();
        this.model = model;
        modelHandlerRegistration = model.addTreeNodeModelEventHandler(TreePresenter.this::handleTreeNodeModelEvent);
        initialiseRootNodes();
    }

    public void setRenderer(@Nonnull TreeNodeRenderer<U> renderer) {
        viewManager.setRenderer(renderer);
    }

    @Override
    public void setChildAdditionPending(@Nonnull TreeNodeView<U> parentView) {
        pendingChangeManager.setChildAdditionPending(parentView);
    }

    @Override
    public void setRemovalPending(@Nonnull TreeNodeView<U> removedView) {
        pendingChangeManager.setRemovalPending(removedView);
    }

    @Override
    public void setRendingChangePending(@Nonnull TreeNodeView view) {
        pendingChangeManager.setRendingChangePending(view);
    }

    @Override
    public void setPendingChangedCancelled(@Nonnull TreeNodeView view) {
        pendingChangeManager.setPendingChangedCancelled(view);
    }

    /**
     * Reloads the tree and ensures that previously selected paths are selected
     */
    public void reload() {
        Collection<Path<K>> selectedKeyPaths = getSelectedKeyPaths();
        setModel(model);
        clearSelection();
        selectedKeyPaths.forEach(path -> setSelected(path, true, () -> {}));
    }

    public void getTreeNodesForUserObjectKey(@Nonnull K userObjectKey,
                                             @Nonnull GetTreeNodesCallback<U> callback) {
        model.getTreeNodesForUserObjectKey(userObjectKey, callback);
    }

    public void setRootNodesExpanded() {
        for (TreeNodeView<U> treeView : getRootViews()) {
            treeView.setExpanded();
        }
    }

    public void setDropHandler(@Nonnull TreeNodeDropHandler<U> dropHandler) {
        dragAndDropManager.setDropHandler(checkNotNull(dropHandler));
    }

    public void clearDropHandler() {
        dragAndDropManager.clearDropHandler();
    }

    @Nonnull
    public HandlerRegistration addSelectionChangeHandler(@Nonnull SelectionChangeEvent.Handler handler) {
        return selectionModel.addSelectionChangeHandler(handler);
    }

    @Nonnull
    public Path<TreeNodeId> getPathToRoot(@Nonnull TreeNodeId fromNode) {
        Optional<TreeNodeView<U>> view = viewManager.getViewIfPresent(fromNode);
        return view.map(theView -> TreeNodeViewTraverser.<U>newTreeNodeViewTraverser().getTreeNodePathToRoot(theView))
                   .orElse(Path.emptyPath());
    }

    @Override
    public void setTreeNodeExpanded(@Nonnull TreeNodeId node) {
        Optional<TreeNodeView<U>> view = viewManager.getViewIfPresent(node);
        view.ifPresent(v -> setTreeNodeHandleState(TreeViewInputEvent.empty(), v, EXPANDED));
    }

    public void clearSelection() {
        selectionModel.clear();
    }

    @Nonnull
    public Set<TreeNode<U>> getSelectedNodes() {
        return selectionModel.getSelectedSet();
    }

    @Nonnull
    public Set<K> getSelectedKeys() {
        return selectionModel.getSelectedSet().stream()
                             .map(node -> model.getKeyProvider().getKey(node.getUserObject()))
                             .collect(toSet());
    }

    @Nonnull
    public Collection<Path<K>> getSelectedKeyPaths() {
        return selectionModel.getSelectedSet().stream()
                             .map(node -> model.getPathToRoot(node.getId()))
                             .map(path -> path.transform(element -> model.getKeyProvider().getKey(element.getUserObject())))
                             .collect(toList());
    }

    @Nonnull
    public Optional<K> getFirstSelectedKey() {
        return selectionModel.getSelectedSet().stream()
                             .map(node -> model.getKeyProvider().getKey(node.getUserObject()))
                             .findFirst();
    }

    @Nonnull
    public Optional<TreeNode<U>> getFirstSelectedNode() {
        return getSelectedNodes().stream().findFirst();
    }

    @Nonnull
    public Optional<U> getFirstSelectedUserObject() {
        return selectionModel.getSelectedSet().stream()
                             .map(TreeNode::getUserObject)
                             .findFirst();
    }

    public boolean isSelected(@Nonnull TreeNode<U> object) {
        return selectionModel.isSelected(object);
    }

    public boolean isSelected(@Nonnull K key) {
        return selectionModel.getSelectedSet().stream()
                             .map(node -> model.getKeyProvider().getKey(node.getUserObject()))
                             .findFirst().isPresent();
    }

    public void setSelected(@Nonnull TreeNode<U> object, boolean selected) {
        selectionModel.setSelected(object, selected);
    }

    public void moveSelectionUp() {
        keyboardEventMapper.moveSelectionUp();
    }

    public void moveSelectionDown() {
        keyboardEventMapper.moveSelectionDown();
    }

    public void setSelected(@Nonnull Path<K> keyPath,
                            boolean selected,
                            @Nonnull Runnable callback) {
        keyPath.getLast().ifPresent(lastKey -> {
            model.getTreeNodesForUserObjectKey(lastKey, nodes -> {
                nodes.forEach(node -> {
                    Path<TreeNodeData<U>> pathToRoot = model.getPathToRoot(node.getId());
                    Path<K> curKeyPath = pathToRoot.transform(element -> model.getKeyProvider().getKey(element.getUserObject()));
                    if (keyPath.equals(curKeyPath)) {
                        pathToRoot.getLast().ifPresent(tn -> setSelected(tn.getTreeNode(), selected));
                    }
                });
                callback.run();
            });
        });
    }

    public void scrollSelectionIntoView() {
        for (TreeNodeView<U> selectedView : treeNodeViewSelectionProvider.getSelection()) {
            selectedView.scrollIntoView();
        }
    }

    public void setExpanded(@Nonnull Path<K> keyPath) {
        keyPath.getLast().ifPresent(lastKey -> {
            model.getTreeNodesForUserObjectKey(lastKey, nodes -> {
                nodes.forEach(node -> {
                    Path<TreeNodeData<U>> pathToRoot = model.getPathToRoot(node.getId());
                    Path<K> curKeyPath = pathToRoot.transform(element -> model.getKeyProvider().getKey(element.getUserObject()));
                    if (keyPath.equals(curKeyPath)) {
                        pathToRoot.forEach(tnd -> viewManager.getViewIfPresent(tnd.getId()).ifPresent(TreeNodeView::setExpanded));
                    }
                });
            });
        });
    }

    public void clearPruning() {
        for (TreeNodeView<U> rootView : getRootViews()) {
            TreeNodeViewTraverser<U> traver = TreeNodeViewTraverser
                    .newTreeNodeViewTraverser();
            Iterator<TreeNodeView<U>> iterator = traver.iterator(rootView);
            while (iterator.hasNext()) {
                TreeNodeView itView = iterator.next();
                itView.setHidden(false);
                itView.setPruned(false);
            }
        }
        scrollSelectionIntoView();
    }

    public void pruneToNodes(@Nonnull Collection<TreeNodeId> treeNodes) {
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
                view.setPruned(true);
                if (!tailNodes.contains(view.getNodeId())) {
                    for (TreeNodeView<U> childView : view.getChildViews()) {
                        queue.add(childView);
                    }
                }
            }
        }
    }

    public void pruneToSelectedNodes() {
        List<TreeNodeId> selNodeIds = selectionModel.getSelectedSet().stream()
                                                    .map(TreeNode::getId)
                                                    .collect(toList());
        pruneToNodes(selNodeIds);
    }

    public void pruneToNodesContainingKey(@Nonnull K key, @Nonnull Runnable callback) {
        model.getTreeNodesForUserObjectKey(key, nodes -> {
            List<TreeNodeId> nodeIds = nodes.stream().map(TreeNodeData::getId).collect(toList());
            pruneToNodes(nodeIds);
            callback.run();
        });
    }

    public void revealTreeNodesForKey(@Nonnull final K userObjectKey,
                                      @Nonnull final RevealMode revealMode) {
        model.getTreeNodesForUserObjectKey(userObjectKey, nodes -> {
            for (TreeNodeData<U> tn : nodes) {
                Path<TreeNodeData<U>> pathToRoot = model.getPathToRoot(tn.getId());
                for (int i = 0; i < pathToRoot.size(); i++) {
                    TreeNodeData<U> treeNodeData = pathToRoot.get(i);
                    if (i < pathToRoot.size() - 1) {
                        viewManager.getView(treeNodeData);
                        setTreeNodeExpanded(treeNodeData.getId());
                    }
                    else {
                        setSelected(tn.getTreeNode(), true);
                        scrollSelectionIntoView();
                    }
                }
                if (revealMode == RevealMode.REVEAL_FIRST) {
                    break;
                }
            }
        });
    }

    @Override
    public void getNodes(@Nonnull Optional<TreeNodeId> parentNode,
                         @Nonnull GetTreeNodesCallback<U> callback) {
        model.getNodes(parentNode, callback);
    }

    private void initialiseRootNodes() {
        treeView.clear();
        model.getNodes(Optional.empty(), nodes -> {
            TreeNodeView<U> previousSibling = null;
            for (TreeNodeData<U> node : nodes) {
                TreeNodeView<U> rootNodeView = viewManager.getView(node);
                treeView.add(rootNodeView.asWidget());
                setTreeNodeExpanded(node.getId());
                if(previousSibling != null) {
                    rootNodeView.setPreviousSibling(previousSibling);
                    previousSibling.setNextSibling(rootNodeView);
                }
                previousSibling = rootNodeView;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private List<TreeNodeView<U>> getRootViews() {
        List<TreeNodeView<U>> result = Lists.newArrayList();
        for (Widget widget : treeView) {
            if (widget instanceof TreeNodeView) {
                result.add((TreeNodeView<U>) widget);
            }
        }
        return result;
    }

    private void handleTreeNodeModelEvent(@Nonnull TreeNodeModelEvent event) {
        pendingChangeManager.handleTreeNodeModelEvent(event);
        for (TreeNodeModelChange change : event.getChanges()) {
            handleTreeNodeModelChange(change);
        }
    }

    private void handleTreeNodeModelChange(@Nonnull TreeNodeModelChange<U> change) {
        GWT.log("[TreePresenter] Handling TreeModelChange: " + change);
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

            public void visit(NodeUserObjectChanged<U> nodeUserObjectChanged) {
                nodeUserObjectChangedHandler.handleNodeUserObjectChanged(nodeUserObjectChanged);
            }
        });
    }

    private void setTreeNodeHandleState(@Nonnull TreeViewInputEvent<U> event,
                                        @Nonnull TreeNodeView<U> treeNodeView,
                                        @Nonnull TreeNodeViewState nextState) {
        if (nextState == TreeNodeViewState.COLLAPSED) {
            new SetTreeNodeCollapsedHandler<U>().invoke(event, Collections.singleton(treeNodeView));
        }
        else {
            new SetTreeNodeExpandedHandler<>(model, selectionModel, viewManager).invoke(event,
                                                                                        Collections.singleton(treeNodeView));
        }
    }
}
