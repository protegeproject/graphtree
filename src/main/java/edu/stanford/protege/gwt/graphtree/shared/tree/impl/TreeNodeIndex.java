package edu.stanford.protege.gwt.graphtree.shared.tree.impl;


import com.google.common.collect.*;
import edu.stanford.protege.gwt.graphtree.shared.UserObjectKeyProvider;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 *
 * An index for tree nodes for storing an looking up tree nodes by their user object,
 * parent node, and child node.
 */
public class TreeNodeIndex<U extends Serializable, K> {

    private final UserObjectKeyProvider<U, K> keyProvider;

    private final Set<TreeNodeId> roots = Sets.newLinkedHashSet();

    private final Map<TreeNodeId, TreeNodeData<U>> id2Data = new HashMap<>();

    private final Multimap<K, TreeNodeData<U>> userObjectKey2Data = HashMultimap.create();

    private final Multimap<TreeNodeId, TreeNodeId> parent2ChildMap = LinkedHashMultimap.create();

    private final Map<TreeNodeId, TreeNodeId> child2ParentMap = Maps.newHashMap();

    public TreeNodeIndex(@Nonnull UserObjectKeyProvider<U, K> keyProvider) {
        this.keyProvider = checkNotNull(keyProvider);
    }


    @Nullable
    public TreeNodeData<U> getTreeNodeData(@Nonnull TreeNodeId treeNodeId) {
        return id2Data.get(treeNodeId);
    }


    public boolean containsChildWithUserObject(@Nonnull TreeNodeId parent, @Nonnull U userObject) {
        K userObjectKey = keyProvider.getKey(userObject);
        for(TreeNodeData nodeWithUserObject : userObjectKey2Data.get(userObjectKey)) {
            TreeNodeId nodeWithUserObjectId = nodeWithUserObject.getId();
            if(parent2ChildMap.containsEntry(parent, nodeWithUserObjectId)) {
                return true;
            }
        }
        return false;
    }

    public void addRoot(@Nonnull TreeNodeData<U> node) {
        if(parent2ChildMap.containsValue(node.getId())) {
            throw new RuntimeException("Node is already a child of another node");
        }
        if (roots.add(node.getId())) {
            id2Data.put(node.getId(), node);
            userObjectKey2Data.put(keyProvider.getKey(node.getUserObject()), node);
        }
    }

    public void removeRoot(@Nonnull TreeNodeId node) {
        if(roots.remove(node)) {
            TreeNodeData<U> data = id2Data.remove(node);
            userObjectKey2Data.remove(keyProvider.getKey(data.getUserObject()), data);
        }
    }

    @Nonnull
    public List<TreeNodeData<U>> getRoots() {
        return roots.stream().map(id2Data::get).collect(toList());
    }

    public boolean addChild(@Nonnull TreeNodeId parentId, @Nonnull TreeNodeData<U> childNodeData) {
        if(parent2ChildMap.containsValue(childNodeData.getId())) {
            throw new RuntimeException("Node is already a child of another node");
        }
        boolean added = parent2ChildMap.put(parentId, childNodeData.getId());
        if(added) {
            id2Data.put(childNodeData.getId(), childNodeData);
            child2ParentMap.put(childNodeData.getId(), parentId);
            userObjectKey2Data.put(keyProvider.getKey(childNodeData.getUserObject()), childNodeData);
        }
        return added;
    }

    public void removeChild(@Nonnull TreeNodeId parentNodeId,
                            @Nonnull TreeNodeId childNodeId,
                            @Nonnull Multimap<TreeNodeId, TreeNodeId> removedBranches) {
        TreeNodeData<U> childData = id2Data.get(childNodeId);
        if(childData == null) {
            return;
        }
        boolean removed = parent2ChildMap.remove(parentNodeId, childData.getId());
        if(removed) {
            child2ParentMap.remove(childNodeId);
            id2Data.remove(childNodeId);
            removedBranches.put(parentNodeId, childNodeId);
            userObjectKey2Data.remove(keyProvider.getKey(childData.getUserObject()), childData);
            for(TreeNodeData<U> grandChildNode : getChildren(childNodeId)) {
                removeChild(childNodeId, grandChildNode.getId(), removedBranches);
            }
        }
    }

    public void updateUserObject(@Nonnull U userObject) {
        K key = keyProvider.getKey(checkNotNull(userObject));
        // Replace TreeNodeData that is identified by the user object key
        new ArrayList<>(userObjectKey2Data.removeAll(key)).forEach(data -> {
            TreeNodeId id = data.getId();
            TreeNodeData<U> replacementData = new TreeNodeData<>(new TreeNode<>(id, userObject), data.isLeaf());
            userObjectKey2Data.put(key, replacementData);
            id2Data.put(id, replacementData);
        });
    }

    @Nonnull
    public Optional<TreeNodeId> getParent(@Nonnull TreeNodeId childNode) {
        return Optional.ofNullable(child2ParentMap.get(childNode));
    }

    @Nonnull
    public List<TreeNodeData<U>> getChildren(@Nonnull TreeNodeId parentId) {
        Collection<TreeNodeId> childIds = parent2ChildMap.get(parentId);
        return childIds.stream().map(id2Data::get).collect(toList());
    }

    @Nonnull
    public List<TreeNodeData<U>> getTreeNodesForUserObjectKey(@Nonnull K userObjectKey) {
        return new ArrayList<>(userObjectKey2Data.get(userObjectKey));
    }
}
