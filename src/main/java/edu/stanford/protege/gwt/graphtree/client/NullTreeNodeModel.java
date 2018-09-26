package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.HashMultimap;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.UserObjectKeyProvider;
import edu.stanford.protege.gwt.graphtree.shared.tree.*;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public class NullTreeNodeModel<U extends Serializable, K> implements TreeNodeModel<U, K> {

    private static final HandlerRegistration NULL_HANDLER_REGISTRATION = () -> {};

    @Nonnull
    @Override
    public HandlerRegistration addTreeNodeModelEventHandler(@Nonnull TreeNodeModelEventHandler handler) {
        return NULL_HANDLER_REGISTRATION;
    }

    @Nonnull
    @Override
    public UserObjectKeyProvider<U, K> getKeyProvider() {
        return userObject -> null;
    }

    @Override
    public void getBranchesContainingUserObjectKey(@Nonnull K userObjectKey, @Nonnull GetBranchesCallback<U> callback) {
        callback.handleBranches(HashMultimap.create());
    }

    @Override
    public void getNodes(@Nonnull Optional<TreeNodeId> parentNode, @Nonnull GetTreeNodesCallback<U> callback) {
        callback.handleNodes(Collections.emptyList());
    }

    @Override
    public void getTreeNodesForUserObjectKey(@Nonnull K userObjectKey, @Nonnull GetTreeNodesCallback<U> callback) {
        callback.handleNodes(Collections.emptyList());
    }

    @Nonnull
    @Override
    public Path<TreeNodeData<U>> getPathToRoot(@Nonnull TreeNodeId treeNodeId) {
        return Path.emptyPath();
    }

    @Override
    public Optional<TreeNode<U>> getTreeNode(@Nonnull TreeNodeId nodeId) {
        return Optional.empty();
    }

    @Override
    public void dispose() {

    }
}
