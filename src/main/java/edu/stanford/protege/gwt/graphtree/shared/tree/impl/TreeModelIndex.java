package edu.stanford.protege.gwt.graphtree.shared.tree.impl;


import com.google.common.base.Optional;
import com.google.common.collect.*;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class TreeModelIndex<U extends Serializable> {

    private Map<TreeNodeId, TreeNodeData<U>> roots = Maps.newLinkedHashMap();

    private Map<TreeNodeId, TreeNodeData<U>> id2Data = new HashMap<TreeNodeId, TreeNodeData<U>>();

    private Multimap<TreeNodeId, TreeNodeData<U>> parent2ChildMap = LinkedHashMultimap.create();

    private Multimap<U, TreeNodeData<U>> userObject2Data = HashMultimap.create();

    private Map<TreeNodeId, TreeNodeId> child2ParentMap = Maps.newHashMap();


    public TreeNodeData<U> getTreeNodeData(TreeNodeId treeNodeId) {
        return id2Data.get(treeNodeId);
    }


    public boolean containsChildWithUserObject(TreeNodeId parent, U userObject) {
        for(TreeNodeData nodeWithUserObject : userObject2Data.get(userObject)) {
            if(parent2ChildMap.containsEntry(parent, nodeWithUserObject)) {
                return true;
            }
        }
        return false;
    }

    public void addRoot(TreeNodeData<U> node) {
        if(parent2ChildMap.containsValue(node)) {
            throw new RuntimeException("Node is already a child of another node");
        }
        roots.put(node.getId(), node);
        id2Data.put(node.getId(), node);
        userObject2Data.put(node.getUserObject(), node);
    }

    public void removeRoot(TreeNodeId node) {
        TreeNodeData<U> removed = roots.remove(node);
        if(removed != null) {
            id2Data.remove(node);
            userObject2Data.remove(removed.getUserObject(), removed);
        }
    }

    public List<TreeNodeData<U>> getRoots() {
        return new ArrayList<TreeNodeData<U>>(roots.values());
    }

    public boolean addChild(TreeNodeId parentId, TreeNodeData<U> childNodeData) {
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

    public void removeChild(TreeNodeId parentNode, TreeNodeId childNode, Multimap<TreeNodeId, TreeNodeId> removedBranches) {
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

    public Optional<TreeNodeId> getParent(TreeNodeId childNode) {
        return Optional.fromNullable(child2ParentMap.get(childNode));
    }

    public List<TreeNodeData<U>> getChildren(TreeNodeId parentId) {
        return new ArrayList<TreeNodeData<U>>(parent2ChildMap.get(parentId));
    }

    public List<TreeNodeData<U>> getTreeNodesForUserObject(U userObject) {
        return new ArrayList<TreeNodeData<U>>(userObject2Data.get(userObject));
    }
}
