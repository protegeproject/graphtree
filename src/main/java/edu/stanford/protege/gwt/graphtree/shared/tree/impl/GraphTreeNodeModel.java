package edu.stanford.protege.gwt.graphtree.shared.tree.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.HandlerManager;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;
import edu.stanford.protege.gwt.graphtree.shared.tree.*;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 09/07/2013 <p>
 * A tree model that caches nodes.  Nodes are loaded lazily into the model from a GraphModel. </p>
 */
public class GraphTreeNodeModel<U extends Serializable> implements TreeNodeModel<U> {

    private final IdGenerator idGenerator = new IdGenerator();

    private GraphModel<U> graphModel;

    private HandlerManager handlerManager;

    private TreeModelIndex<U> treeModelIndex = new TreeModelIndex<>();

    private Set<TreeNodeId> loadedNodes;

    /**
     * Constructs a GraphTreeNodeModel.
     *
     * @param graphModel The graph that the model is based on.
     */
    public GraphTreeNodeModel(GraphModel<U> graphModel) {
        this.graphModel = graphModel;
        this.loadedNodes = Sets.newHashSet();
        this.handlerManager = new HandlerManager(this);
        // TODO: Handler reg.  Move attachment out of this constructor.
        graphModel.addGraphModelHandler(event -> handleGraphModelChanges(event.getChanges()));
    }

    public static <U extends Serializable> GraphTreeNodeModel<U> create(GraphModel<U> graphModel) {
        return new GraphTreeNodeModel<>(graphModel);
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
    public void getBranchesContainingUserObject(@Nonnull U userObject, @Nonnull final GetBranchesCallback<U> callback) {
        getTreeNodesForUserObject(userObject, nodes -> {
            Multimap<TreeNodeData<U>, TreeNodeData<U>> branches = HashMultimap.create();
            for (TreeNodeData<U> node : nodes) {
                Optional<TreeNodeId> parentNode = treeModelIndex.getParent(node.getId());
                while (parentNode.isPresent()) {
                    branches.putAll(treeModelIndex.getTreeNodeData(parentNode.get()),
                                    treeModelIndex.getChildren(parentNode.get()));
                    parentNode = treeModelIndex.getParent(parentNode.get());
                }
            }
            callback.handleBranches(branches);
        });
    }

    @Nonnull
    @Override
    public Path<TreeNodeData<U>> getPathToRoot(@Nonnull TreeNodeId treeNodeId) {
        List<TreeNodeData<U>> result = new ArrayList<>();
        result.add(treeModelIndex.getTreeNodeData(treeNodeId));
        Optional<TreeNodeId> parentNode = treeModelIndex.getParent(treeNodeId);
        while (parentNode.isPresent()) {
            TreeNodeId theParentNodeId = parentNode.get();
            result.add(0, treeModelIndex.getTreeNodeData(theParentNodeId));
            parentNode = treeModelIndex.getParent(theParentNodeId);
        }
        return new Path<>(result);
    }

    @Override
    public void getTreeNodesForUserObject(@Nonnull final U userObject, @Nonnull final GetTreeNodesCallback<U> callback) {
        // We need to make sure that the nodes in the graph that have the specified user object as their user object
        // have corresponding tree nodes.
        // We load nodes by loading their parent node.  Search for paths from the key nodes to graph nodes containing
        // the user object and then make sure that the nodes on these paths are loaded.
        graphModel.getPathsFromKeyNodes(userObject, paths -> {
            // Now we have the paths, ensure that each node on the path is loaded as a tree node
            ensureAllPathNodesAreLoaded(paths, () -> {
                List<TreeNodeData<U>> treeNodes = treeModelIndex.getTreeNodesForUserObject(userObject);
                callback.handleNodes(treeNodes);
            });
        });
    }

    private void handleGraphModelChanges(List<GraphModelChange<U>> changesList) {
        List<GraphModelChange<U>> topologicallyOrderedChanges = new GraphModelChangeTidier<>(changesList)
                .getTidiedChanges();
        final List<TreeNodeModelChange> resultingChanges = new ArrayList<>();
        for (GraphModelChange<U> change : topologicallyOrderedChanges) {
            change.accept(new GraphModelChangeVisitor<U>() {
                public void visit(AddKeyNode<U> addKeyNode) {
                    handleAddKeyNode(addKeyNode, resultingChanges);
                }

                public void visit(RemoveKeyNode<U> removeKeyNode) {
                    handleRemoveKeyNode(removeKeyNode, resultingChanges);
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

    private void handleAddKeyNode(AddKeyNode<U> addKeyNode, List<TreeNodeModelChange> resultingChanges) {
        GraphNode<U> keyNode = addKeyNode.getNode();
        TreeNodeData<U> rootNode = new TreeNodeData<>(new TreeNode<>(idGenerator.getNextId(),
                                                                     keyNode.getUserObject()), keyNode.isSink());
        treeModelIndex.addRoot(rootNode);
        resultingChanges.add(new RootNodeAdded<>(rootNode));
    }

    private void handleRemoveKeyNode(RemoveKeyNode removeKeyNode, List<TreeNodeModelChange> resultingChanges) {
        for (TreeNodeData<U> rootNode : treeModelIndex.getRoots()) {
            GraphNode keyNode = removeKeyNode.getNode();
            if (rootNode.getUserObject().equals(keyNode.getUserObject())) {
                treeModelIndex.removeRoot(rootNode.getId());
                resultingChanges.add(new RootNodeRemoved<U>(rootNode.getId()));
            }
        }
    }

    private void handleAddEdge(AddEdge<U> addEdge, List<TreeNodeModelChange> resultingChanges) {
        U predecessorUserObject = addEdge.getPredecessor().getUserObject();
        List<TreeNodeData<U>> parentNodes = treeModelIndex.getTreeNodesForUserObject(predecessorUserObject);
        for (TreeNodeData<U> parentNode : parentNodes) {
            GraphNode<U> successorNode = addEdge.getSuccessor();
            U successorUserObject = addEdge.getSuccessor().getUserObject();
            if (!treeModelIndex.containsChildWithUserObject(parentNode.getId(), successorUserObject)) {
                TreeNodeData<U> childNode = new TreeNodeData<>(new TreeNode<>(idGenerator.getNextId(),
                                                                              successorNode.getUserObject()),
                                                               successorNode.isSink());
                boolean added = treeModelIndex.addChild(parentNode.getId(), childNode);
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
        for (TreeNodeData<U> parentNode : treeModelIndex.getTreeNodesForUserObject(predecessorUserObject)) {
            GraphNode<U> successor = removeEdge.getSuccessor();
            removeChild(parentNode.getId(), successor, resultingChanges);
        }
    }

    private void handleUpdateUserObject(UpdateUserObject<U> updateUserObject, List<TreeNodeModelChange> resultingChanges) {
        U userObject = updateUserObject.getGraphNode().getUserObject();
        for (TreeNodeData<U> node : treeModelIndex.getTreeNodesForUserObject(userObject)) {
            resultingChanges.add(new NodeRenderingChanged<>(node.getId(), node.getUserObject()));
        }
    }

    private void removeChild(TreeNodeId parentNode,
                             GraphNode<?> successor,
                             List<TreeNodeModelChange> resultingChanges) {
        Multimap<TreeNodeId, TreeNodeId> removedBranches = LinkedHashMultimap.create();
        for (TreeNodeData<U> childNode : treeModelIndex.getChildren(parentNode)) {
            if (childNode.getUserObject().equals(successor.getUserObject())) {
                treeModelIndex.removeChild(parentNode, childNode.getId(), removedBranches);
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
        if (treeModelIndex.getRoots().isEmpty()) {
            loadRoots(callback);
        }
        else {
            callback.handleNodes(treeModelIndex.getRoots());
        }
    }

    private void loadRoots(final GetTreeNodesCallback<U> callback) {
        graphModel.getKeyNodes(keyNodes -> {
            for (GraphNode<U> keyNode : keyNodes) {
                TreeNodeData<U> treeNode = new TreeNodeData<>(new TreeNode<>(idGenerator.getNextId(),
                                                                             keyNode.getUserObject()),
                                                              keyNode.isSink());
                treeModelIndex.addRoot(treeNode);
            }
            callback.handleNodes(treeModelIndex.getRoots());
        });
    }

    private void getChildNodes(final TreeNodeId parentNode, final GetTreeNodesCallback<U> callback) {
        if (!isLoaded(parentNode)) {
            loadChildren(parentNode, callback);
        }
        else {
            callback.handleNodes(treeModelIndex.getChildren(parentNode));
        }
    }

    private void loadChildren(final TreeNodeId parentNode, final GetTreeNodesCallback<U> callback) {
        final TreeNodeData<U> data = treeModelIndex.getTreeNodeData(parentNode);
        graphModel.getSuccessorNodes(data.getUserObject(), successors -> {
            loadedNodes.add(parentNode);
            for (GraphNode<U> successor : successors.getSuccessors()) {
                if (!treeModelIndex.containsChildWithUserObject(parentNode, successor.getUserObject())) {
                    TreeNodeData<U> childNode = new TreeNodeData<>(new TreeNode<>(idGenerator.getNextId(),
                                                                                  successor.getUserObject()),
                                                                   successor.isSink());
                    treeModelIndex.addChild(parentNode, childNode);
                }
            }
            callback.handleNodes(treeModelIndex.getChildren(parentNode));
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
//            List<TreeNodeData<U>> children = treeModelIndex.getChildren(node.getId());
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
