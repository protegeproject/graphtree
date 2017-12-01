package edu.stanford.protege.gwt.graphtree.shared.tree.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.UserObjectKeyProvider;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;
import edu.stanford.protege.gwt.graphtree.shared.tree.*;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 09/07/2013 <p>
 * A tree model that caches nodes.  Nodes are loaded lazily into the model from a GraphModel. </p>
 */
public class GraphTreeNodeModel<U extends Serializable, K> implements TreeNodeModel<U, K> {

    private final IdGenerator idGenerator = new IdGenerator();

    private final GraphModel<U, K> graphModel;

    private final HandlerManager handlerManager;

    private final TreeNodeIndex<U, K> treeNodeIndex;

    private final Set<TreeNodeId> loadedNodes = new HashSet<>();

    private final UserObjectKeyProvider<U, K> keyProvider;

    @Nonnull
    private HandlerRegistration graphModelHandlerRegistration = () -> {};

    /**
     * Constructs a GraphTreeNodeModel.
     *
     * @param graphModel The graph that the model is based on.
     */
    private GraphTreeNodeModel(@Nonnull GraphModel<U, K> graphModel,
                               @Nonnull UserObjectKeyProvider<U, K> keyProvider) {
        this.graphModel = checkNotNull(graphModel);
        this.keyProvider = checkNotNull(keyProvider);
        this.treeNodeIndex = new TreeNodeIndex<>(keyProvider);
        this.handlerManager = new HandlerManager(this);
    }

    /**
     * A factory method for creating a new {@link GraphTreeNodeModel}.  The model will attach itself
     * as a listener to the specified graph model so that changes in the graph model are reflected by
     * changes in this tree model.  Node that the {@link #dispose()} method should be called to remove
     * the listener that is attached.
     * @param graphModel The graph model.
     */
    public static <U extends Serializable, K> GraphTreeNodeModel<U, K> create(@Nonnull GraphModel<U, K> graphModel,
                                                                        @Nonnull UserObjectKeyProvider<U, K> keyProvider) {
        GraphTreeNodeModel<U, K> treeNodeModel = new GraphTreeNodeModel<>(graphModel, keyProvider);
        treeNodeModel.attachListeners();
        return treeNodeModel;
    }

    private void attachListeners() {
        graphModelHandlerRegistration = graphModel.addGraphModelHandler(event -> handleGraphModelChanges(event.getChanges()));
    }

    @Override
    public void dispose() {
        graphModelHandlerRegistration.removeHandler();
    }

    private static <U extends Serializable> Optional<TreeNodeData<U>> getNodeWithUserObject(U userObject, Collection<TreeNodeData<U>> inCollection) {
        for (TreeNodeData<U> candidate : inCollection) {
            if (candidate.getUserObject().equals(userObject)) {
                return Optional.of(candidate);
            }
        }
        return Optional.empty();
    }

    @Nonnull
    public HandlerRegistration addTreeNodeModelEventHandler(@Nonnull TreeNodeModelEventHandler handler) {
        return handlerManager.addHandler(TreeNodeModelEvent.getType(), handler);
    }

    public void getNodes(@Nonnull Optional<TreeNodeId> parentNode, @Nonnull GetTreeNodesCallback<U> callback) {
        if (parentNode.isPresent()) {
            getChildNodes(parentNode.get(), callback);
        }
        else {
            getRootNodes(callback);
        }
    }

    @Override
    public void getBranchesContainingUserObjectKey(@Nonnull K userObjectKey, @Nonnull final GetBranchesCallback<U> callback) {
        getTreeNodesForUserObjectKey(userObjectKey, nodes -> {
            Multimap<TreeNodeData<U>, TreeNodeData<U>> branches = HashMultimap.create();
            for (TreeNodeData<U> node : nodes) {
                Optional<TreeNodeId> parentNode = treeNodeIndex.getParent(node.getId());
                while (parentNode.isPresent()) {
                    branches.putAll(treeNodeIndex.getTreeNodeData(parentNode.get()),
                                    treeNodeIndex.getChildren(parentNode.get()));
                    parentNode = treeNodeIndex.getParent(parentNode.get());
                }
            }
            callback.handleBranches(branches);
        });
    }

    @Nonnull
    @Override
    public Path<TreeNodeData<U>> getPathToRoot(@Nonnull TreeNodeId treeNodeId) {
        List<TreeNodeData<U>> result = new ArrayList<>();
        result.add(treeNodeIndex.getTreeNodeData(treeNodeId));
        Optional<TreeNodeId> parentNode = treeNodeIndex.getParent(treeNodeId);
        while (parentNode.isPresent()) {
            TreeNodeId theParentNodeId = parentNode.get();
            result.add(0, treeNodeIndex.getTreeNodeData(theParentNodeId));
            parentNode = treeNodeIndex.getParent(theParentNodeId);
        }
        return new Path<>(result);
    }

    @Override
    public void getTreeNodesForUserObjectKey(@Nonnull final K userObjectKey, @Nonnull final GetTreeNodesCallback<U> callback) {
        // We need to make sure that the nodes in the graph that have the specified user object as their user object
        // have corresponding tree nodes.
        // We load nodes by loading their parent node.  Search for paths from the key nodes to graph nodes containing
        // the user object and then make sure that the nodes on these paths are loaded.
        graphModel.getPathsFromRootNodes(userObjectKey, paths -> {
            // Now we have the paths, ensure that each node on the path is loaded as a tree node
            ensureAllPathNodesAreLoaded(paths, () -> {
                List<TreeNodeData<U>> treeNodes = treeNodeIndex.getTreeNodesForUserObjectKey(userObjectKey);
                callback.handleNodes(treeNodes);
            });
        });
    }

    private void handleGraphModelChanges(List<GraphModelChange<U>> changesList) {
        GWT.log("[GraphTreeNodeModel] Handling graph model changes: " + changesList);
        List<GraphModelChange<U>> topologicallyOrderedChanges = new GraphModelChangeTidier<>(changesList)
                .getTidiedChanges();
        final List<TreeNodeModelChange> resultingChanges = new ArrayList<>();
        for (GraphModelChange<U> change : topologicallyOrderedChanges) {
            GWT.log("[GraphTreeNodeModel] Handling graph model changes.  Change: " + change);
            change.accept(new GraphModelChangeVisitor<U>() {
                public void visit(AddRootNode<U> addRootNode) {
                    handleAddKeyNode(addRootNode, resultingChanges);
                }

                public void visit(RemoveRootNode<U> removeRootNode) {
                    handleRemoveKeyNode(removeRootNode, resultingChanges);
                }

                public void visit(AddEdge<U> addEdge) {
                    handleAddEdge(addEdge, resultingChanges);
                }

                public void visit(RemoveEdge<U> removeEdge) {
                    handleRemoveEdge(removeEdge, resultingChanges);
                }

                public void visit(UpdateUserObject<U> updateUserObject) {
                    handleUpdateUserObject(updateUserObject, resultingChanges);
                }
            });
        }
        handlerManager.fireEvent(new TreeNodeModelEvent(resultingChanges));
    }

    private void handleAddKeyNode(AddRootNode<U> addRootNode, List<TreeNodeModelChange> resultingChanges) {
        GraphNode<U> keyNode = addRootNode.getNode();
        TreeNodeData<U> rootNode = new TreeNodeData<>(new TreeNode<>(idGenerator.getNextId(),
                                                                     keyNode.getUserObject()), keyNode.isSink());
        treeNodeIndex.addRoot(rootNode);
        resultingChanges.add(new RootNodeAdded<>(rootNode));
    }

    private void handleRemoveKeyNode(RemoveRootNode removeRootNode, List<TreeNodeModelChange> resultingChanges) {
        for (TreeNodeData<U> rootNode : treeNodeIndex.getRoots()) {
            GraphNode keyNode = removeRootNode.getNode();
            if (rootNode.getUserObject().equals(keyNode.getUserObject())) {
                treeNodeIndex.removeRoot(rootNode.getId());
                resultingChanges.add(new RootNodeRemoved<U>(rootNode.getId()));
            }
        }
    }

    private void handleAddEdge(AddEdge<U> addEdge, List<TreeNodeModelChange> resultingChanges) {
        U predecessorUserObject = addEdge.getPredecessor().getUserObject();
        K predecessorUserObjectKey = keyProvider.getKey(predecessorUserObject);
        List<TreeNodeData<U>> parentNodes = treeNodeIndex.getTreeNodesForUserObjectKey(predecessorUserObjectKey);
        for (TreeNodeData<U> parentNode : parentNodes) {
            GraphNode<U> successorNode = addEdge.getSuccessor();
            U successorUserObject = addEdge.getSuccessor().getUserObject();
            if (!treeNodeIndex.containsChildWithUserObject(parentNode.getId(), successorUserObject)) {
                TreeNodeData<U> childNode = new TreeNodeData<>(new TreeNode<>(idGenerator.getNextId(),
                                                                              successorNode.getUserObject()),
                                                               successorNode.isSink());
                boolean added = treeNodeIndex.addChild(parentNode.getId(), childNode);
                if (added) {
                    resultingChanges.add(new ChildNodeAdded<>(parentNode.getId(), childNode));
                }
                else {
                    loadedNodes.remove(parentNode.getId());
                }
            }
        }
    }

    private void handleRemoveEdge(RemoveEdge<U> removeEdge, List<TreeNodeModelChange> resultingChanges) {
        U predecessorUserObject = removeEdge.getPredecessor().getUserObject();
        K predecessorKey = keyProvider.getKey(predecessorUserObject);
        for (TreeNodeData<U> parentNode : treeNodeIndex.getTreeNodesForUserObjectKey(predecessorKey)) {
            GraphNode<U> successor = removeEdge.getSuccessor();
            removeChild(parentNode.getId(), successor, resultingChanges);
        }
    }

    private void handleUpdateUserObject(UpdateUserObject<U> updateUserObject, List<TreeNodeModelChange> resultingChanges) {
        U userObject = updateUserObject.getUserObject();
        K userObjectKey = keyProvider.getKey(userObject);
        GWT.log("[GraphTreeNodeModel] Updating user object: " + userObject);
        GWT.log("[GraphTreeNodeModel] Key for user object: " + userObjectKey);
        treeNodeIndex.updateUserObject(userObject);
        for (TreeNodeData<U> node : treeNodeIndex.getTreeNodesForUserObjectKey(userObjectKey)) {
            resultingChanges.add(new NodeUserObjectChanged<>(node.getId(), node.getUserObject()));
        }
    }

    private void removeChild(TreeNodeId parentNode,
                             GraphNode<?> successor,
                             List<TreeNodeModelChange> resultingChanges) {
        Multimap<TreeNodeId, TreeNodeId> removedBranches = LinkedHashMultimap.create();
        for (TreeNodeData<U> childNode : treeNodeIndex.getChildren(parentNode)) {
            if (childNode.getUserObject().equals(successor.getUserObject())) {
                treeNodeIndex.removeChild(parentNode, childNode.getId(), removedBranches);
                if (!removedBranches.isEmpty()) {
                    for (TreeNodeId removedParentNode : removedBranches.keySet()) {
                        for (TreeNodeId removedChildNode : removedBranches.get(removedParentNode)) {
                            resultingChanges.add(new ChildNodeRemoved(removedParentNode, removedChildNode));
                        }
                    }
                }
                else {
                    loadedNodes.remove(parentNode);
                }
            }
        }
    }

    private void getRootNodes(final GetTreeNodesCallback<U> callback) {
        if (treeNodeIndex.getRoots().isEmpty()) {
            loadRoots(callback);
        }
        else {
            callback.handleNodes(treeNodeIndex.getRoots());
        }
    }

    private void loadRoots(final GetTreeNodesCallback<U> callback) {
        graphModel.getRootNodes(keyNodes -> {
            for (GraphNode<U> keyNode : keyNodes) {
                TreeNodeData<U> treeNode = new TreeNodeData<>(new TreeNode<>(idGenerator.getNextId(),
                                                                             keyNode.getUserObject()),
                                                              keyNode.isSink());
                treeNodeIndex.addRoot(treeNode);
            }
            callback.handleNodes(treeNodeIndex.getRoots());
        });
    }

    private void getChildNodes(final TreeNodeId parentNode, final GetTreeNodesCallback<U> callback) {
        if (!isLoaded(parentNode)) {
            loadChildren(parentNode, callback);
        }
        else {
            callback.handleNodes(treeNodeIndex.getChildren(parentNode));
        }
    }

    private void loadChildren(final TreeNodeId parentNode, final GetTreeNodesCallback<U> callback) {
        final TreeNodeData<U> data = treeNodeIndex.getTreeNodeData(parentNode);
        K userObjectKey = keyProvider.getKey(data.getUserObject());
        graphModel.getSuccessorNodes(userObjectKey, successors -> {
            loadedNodes.add(parentNode);
            for (GraphNode<U> successor : successors.getSuccessors()) {
                if (!treeNodeIndex.containsChildWithUserObject(parentNode, successor.getUserObject())) {
                    TreeNodeData<U> childNode = new TreeNodeData<>(new TreeNode<>(idGenerator.getNextId(),
                                                                                  successor.getUserObject()),
                                                                   successor.isSink());
                    treeNodeIndex.addChild(parentNode, childNode);
                }
            }
            callback.handleNodes(treeNodeIndex.getChildren(parentNode));
        });
    }

    /**
     * Loads the tree nodes in the specified paths.  If a path is [A, B, C] the the child nodes of A, B and C will be
     * loaded.
     *
     * @param paths    The paths that describe the branches to be loaded.
     * @param callback A callback which is called when loading is complete.
     */
    private void ensureAllPathNodesAreLoaded(final Collection<Path<GraphNode<U>>> paths,
                                             final LoadPathBranchesCallback<U> callback) {
        final Set<Path<GraphNode<U>>> loadedPaths = new HashSet<>();
        for (final Path<GraphNode<U>> path : paths) {
            ensurePathNodesAreLoaded(path.transform(GraphNode::getUserObject),
                                     () -> {
                                         loadedPaths.add(path);
                                         if (loadedPaths.size() == paths.size()) {
                                             callback.pathsLoaded();
                                         }
                                     });
        }
    }

    /**
     * Loads the branch rooted at the given path.
     *
     * @param pathToRoot The path to the root.
     * @param callback   The callback which is called when the path is loaded.
     */
    private void ensurePathNodesAreLoaded(final Path<U> pathToRoot, final LoadPathCallback<U> callback) {

        getRootNodes(roots -> loadTreeNodesAtPathIndex(pathToRoot, 0, roots, callback, new ArrayList<>()));
    }

    private void loadTreeNodesAtPathIndex(final Path<U> path,
                                          final int pathIndex,
                                          final Collection<TreeNodeData<U>> candidateNodes,
                                          final LoadPathCallback<U> callback,
                                          final List<TreeNodeData<U>> loadedPath) {
        Optional<TreeNodeData<U>> node = getNodeWithUserObject(path.get(pathIndex), candidateNodes);
        node.ifPresent(loadedPath::add);
        if (pathIndex == path.getLength() - 1) {
            callback.pathLoaded();
            return;
        }
        if (!node.isPresent()) {
            // Something has gone wrong
            return;
        }
        TreeNodeId nodeId = node.get().getId();
        getChildNodes(nodeId,
                      nodes -> loadTreeNodesAtPathIndex(path, pathIndex + 1, nodes, callback, loadedPath));
//        if (!isLoaded(nodeId)) {
//            loadChildren(nodeId, new GetTreeNodesCallback<U>() {
//                @Override
//                public void handleNodes(List<TreeNodeData<U>> children) {
//                    loadTreeNodesAtPathIndex(path, pathIndex + 1, children, callback);
//                }
//            });
//        }
//        else {
//            List<TreeNodeData<U>> children = treeNodeIndex.getChildren(node.getId());
//            loadTreeNodesAtPathIndex(path, pathIndex + 1, children, callback);
//        }
//        for (TreeNodeData<U> node : candidateNodes) {
//            if (node.getUserObject().equals(path.get(pathIndex))) {
//
//                break;
//            }
//        }
    }

    /**
     * Determines if a node is loaded.
     *
     * @param node The node to test.
     * @return {@code true} if {@code node} is loaded otherwise {@code false}.
     */
    private boolean isLoaded(TreeNodeId node) {
        return loadedNodes.contains(node);
    }

    private interface LoadPathCallback<U extends Serializable> {
        void pathLoaded();
    }

    private interface LoadPathBranchesCallback<U extends Serializable> {
        void pathsLoaded();
    }

    private static class IdGenerator {

        private int id = 0;

        public TreeNodeId getNextId() {
            id++;
            return new TreeNodeId(id);
        }
    }

    private class LoadPathElement implements GetTreeNodesCallback<U> {

        private U currentElement;

        private Iterator<U> nextElements;

        private LoadPathCallback<U> callback;

        private LoadPathElement(U currentElement, Iterator<U> nextElements, LoadPathCallback<U> callback) {
            this.currentElement = currentElement;
            this.nextElements = nextElements;
            this.callback = callback;
        }

        @Override
        public void handleNodes(List<TreeNodeData<U>> nodes) {
            if (nextElements.hasNext()) {
                for (TreeNodeData<U> node : nodes) {
                    if (node.getTreeNode().equals(currentElement)) {
                        getChildNodes(node.getId(), new LoadPathElement(nextElements.next(), nextElements, callback));
                    }
                }
            }
            else {
                callback.pathLoaded();
            }
        }
    }
}
