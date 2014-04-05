package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.gwt.dom.client.Element;
import com.google.inject.Inject;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/01/2014
 */
public class TreeNodeViewManager<U extends Serializable> implements TreeNodeViewMapper<U> {

    public static final String TREE_NODE_VIEW_STYLE_NAME = "tree-node-view";

    private Map<TreeNodeId, TreeNodeView<U>> node2viewMap = HashBiMap.create();

    private Map<Element, TreeNodeView<U>> element2TreeNodeMap = Maps.newHashMap();

//    private Multimap<Object, TreeNodeView<U>> key2ViewMap = HashMultimap.create();

    @Inject
    public TreeNodeViewManager() {
    }

    public void purge() {
        node2viewMap.clear();
        element2TreeNodeMap.clear();
//        key2ViewMap.clear();
    }

    public Optional<TreeNodeView<U>> getViewIfPresent(TreeNodeId node) {
        return Optional.fromNullable(node2viewMap.get(node));
    }

//    public Collection<TreeNodeView<U>> getViewsForKey(Object key) {
//        return key2ViewMap.get(key);
//    }

    public Optional<TreeNodeView<U>> getTreeNodeView(Element element) {
        return Optional.fromNullable(element2TreeNodeMap.get(element));
    }


    public TreeNodeView<U> getView(TreeNodeData<U> treeNode) {
        TreeNodeView<U> view = node2viewMap.get(treeNode.getId());
        if(view != null) {
            return view;
        }
        view = createView(treeNode);
        node2viewMap.put(treeNode.getId(), view);
        element2TreeNodeMap.put(view.asWidget().getElement(), view);
        Object userObject = treeNode.getUserObject();
//        key2ViewMap.put(userObject, view);
        return view;
    }

    private TreeNodeView<U> createView(TreeNodeData<U> treeNode) {
        final TreeNodeView<U> view = new TreeNodeViewImpl<U>(treeNode.getTreeNode());
//        view.setNode(treeNode);
        view.setRendering(treeNode.getHtmlRendering());
        view.setLeaf(treeNode.isLeaf());
        view.asWidget().addStyleName(TREE_NODE_VIEW_STYLE_NAME);
        return view;
    }

    public void releaseView(TreeNodeId node) {
        TreeNodeView view = node2viewMap.remove(node);
        if(view == null) {
            return;
        }
        element2TreeNodeMap.remove(view.asWidget().getElement());
//        key2ViewMap.remove(node.getUserObject(), view);
    }
}
