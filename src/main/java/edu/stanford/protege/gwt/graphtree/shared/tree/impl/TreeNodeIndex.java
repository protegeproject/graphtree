package edu.stanford.protege.gwt.graphtree.shared.tree.impl;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 *
 * An index for tree nodes for storing an looking up tree nodes by their user object,
 * parent node, and child node.
 */
public class TreeNodeIndex<U extends Serializable> {

    private final Map<TreeNodeId, TreeNodeData<U>> roots = Maps.newLinkedHashMap();

    private final Map<TreeNodeId, TreeNodeData<U>> id2Data = new HashMap<>();

    private final Multimap<TreeNodeId, TreeNodeData<U>> parent2ChildMap = LinkedHashMultimap.create();

    private final Multimap<U, TreeNodeData<U>> userObject2Data = HashMultimap.create();

    private final Map<TreeNodeId, TreeNodeId> child2ParentMap = Maps.newHashMap();

    public TreeNodeIndex() {
    }


    @Nullable
    public TreeNodeData<U> getTreeNodeData(@Nonnull TreeNodeId treeNodeId) {
        return id2Data.get(treeNodeId);
    }


    public boolean containsChildWithUserObject(@Nonnull TreeNodeId parent, @Nonnull U userObject) {
        for(TreeNodeData nodeWithUserObject : userObject2Data.get(userObject)) {
            if(parent2ChildMap.containsEntry(parent, nodeWithUserObject)) {
                return true;
            }
        }
        return false;
    }

    public void addRoot(@Nonnull TreeNodeData<U> node) {
        if(parent2ChildMap.containsValue(node)) {
            throw new RuntimeException("Node is already a child of another node");
        }
        roots.put(node.getId(), node);
        id2Data.put(node.getId(), node);
        userObject2Data.put(node.getUserObject(), node);
    }

    public void removeRoot(@Nonnull TreeNodeId node) {
        TreeNodeData<U> removed = roots.remove(node);
        if(removed != null) {
            id2Data.remove(node);
            userObject2Data.remove(removed.getUserObject(), removed);
        }
    }

    @Nonnull
    public List<TreeNodeData<U>> getRoots() {
        return new ArrayList<>(roots.values());
    }

    public boolean addChild(@Nonnull TreeNodeId parentId, @Nonnull TreeNodeData<U> childNodeData) {
        if(parent2ChildMap.containsValue(childNodeData)) {
            throw new RuntimeException("Node is already a child of another node");
        }
        boolean added = parent2ChildMap.put(parentId, childNodeData);
        if(added) {
            id2Data.put(childNodeData.getId(), childNodeData);
            child2ParentMap.put(childNodeData.getId(), parentId);
            userObject2Data.put(childNodeData.getUserObject(), childNodeData);
        }
        return added;
    }

    public void removeChild(@Nonnull TreeNodeId parentNode,
                            @Nonnull TreeNodeId childNode,
                            @Nonnull Multimap<TreeNodeId, TreeNodeId> removedBranches) {
        TreeNodeData<U> childData = id2Data.get(childNode);
        if(childData == null) {
            return;
        }
        boolean removed = parent2ChildMap.remove(parentNode, childData);
        if(removed) {
            child2ParentMap.remove(childNode);
            id2Data.remove(childNode);
            removedBranches.put(parentNode, childNode);
            userObject2Data.remove(childData.getUserObject(), childData);
            for(TreeNodeData<U> grandChildNode : getChildren(childNode)) {
                removeChild(childNode, grandChildNode.getId(), removedBranches);
            }
        }
    }

    @Nonnull
    public Optional<TreeNodeId> getParent(@Nonnull TreeNodeId childNode) {
        return Optional.ofNullable(child2ParentMap.get(childNode));
    }

    @Nonnull
    public List<TreeNodeData<U>> getChildren(@Nonnull TreeNodeId parentId) {
        return new ArrayList<>(parent2ChildMap.get(parentId));
    }

    @Nonnull
    public List<TreeNodeData<U>> getTreeNodesForUserObject(@Nonnull U userObject) {
        return new ArrayList<>(userObject2Data.get(userObject));
    }
}
