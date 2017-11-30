package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.HashMultimap;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.Path;
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
public class NullTreeNodeModel<U extends Serializable> implements TreeNodeModel<U> {

    private static final HandlerRegistration NULL_HANDLER_REGISTRATION = () -> {};

    @Override
    public HandlerRegistration addTreeNodeModelEventHandler(TreeNodeModelEventHandler handler) {
        return NULL_HANDLER_REGISTRATION;
    }

    @Override
    public void getBranchesContainingUserObject(U userObject, GetBranchesCallback<U> callback) {
        callback.handleBranches(HashMultimap.<TreeNodeData<U>, TreeNodeData<U>>create());
    }

    @Override
    public void getNodes(@Nonnull Optional<TreeNodeId> parentNode, @Nonnull GetTreeNodesCallback<U> callback) {
        callback.handleNodes(Collections.<TreeNodeData<U>>emptyList());
    }

    @Override
    public void getTreeNodesForUserObject(@Nonnull U userObject, @Nonnull GetTreeNodesCallback<U> callback) {
        callback.handleNodes(Collections.<TreeNodeData<U>>emptyList());
    }

    @Override
    public Path<TreeNodeData<U>> getPathToRoot(TreeNodeId treeNodeId) {
        return Path.emptyPath();
    }
}
