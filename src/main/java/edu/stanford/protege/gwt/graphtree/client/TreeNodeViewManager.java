package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.gwt.dom.client.Element;
import com.google.inject.Inject;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

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
        return Optional.ofNullable(node2viewMap.get(node));
    }

    public Optional<TreeNodeView<U>> getTreeNodeView(Element element) {
        return Optional.ofNullable(element2TreeNodeMap.get(element));
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

    private TreeNodeView<U> createView(TreeNodeData<U> data) {
        final TreeNodeView<U> view = new TreeNodeViewImpl<U>(data.getId(), data.getUserObject());
        view.setRendering(renderer.getHtmlRendering(data.getUserObject()));
        view.setLeaf(data.isLeaf());
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
