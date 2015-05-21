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

    private TreeNodeRenderer<U> renderer;

    @Inject
    public TreeNodeViewManager(TreeNodeRenderer<U> renderer) {
        this.renderer = renderer;
    }

    public void purge() {
        node2viewMap.clear();
        element2TreeNodeMap.clear();
    }

    public Optional<TreeNodeView<U>> getViewIfPresent(TreeNodeId node) {
        return Optional.fromNullable(node2viewMap.get(node));
    }

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
        return view;
    }

    private TreeNodeView<U> createView(TreeNodeData<U> treeNode) {
        final TreeNodeView<U> view = new TreeNodeViewImpl<U>(treeNode.getTreeNode());
        view.setRendering(renderer.getHtmlRendering(treeNode));
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
    }
}
