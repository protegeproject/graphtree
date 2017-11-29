package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.tree.NodeRenderingChanged;

import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
public class NodeRenderingChangedHandler<U extends Serializable> {

    private TreeNodeViewMapper<U> viewMapper;

    public NodeRenderingChangedHandler(TreeNodeViewMapper<U> viewMapper) {
        this.viewMapper = viewMapper;
    }

    public void handleNodeRenderingChanged(NodeRenderingChanged<U> nodeRenderingChanged) {
        Optional<TreeNodeView<U>> view = viewMapper.getViewIfPresent(nodeRenderingChanged.getTreeNode());
        if (view.isPresent()) {
            view.get().setRendering(nodeRenderingChanged.getHtmlRendering());
        }
    }
}
