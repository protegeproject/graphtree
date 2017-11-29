package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.tree.NodeRenderingChanged;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
public class NodeRenderingChangedHandler<U extends Serializable> {

    private TreeNodeViewMapper<U> viewMapper;
    private TreeNodeRenderer<U> treeNodeRenderer;

    public NodeRenderingChangedHandler(@Nonnull TreeNodeViewMapper<U> viewMapper,
                                       @Nonnull TreeNodeRenderer<U> treeNodeRenderer) {
        this.viewMapper = checkNotNull(viewMapper);
        this.treeNodeRenderer = checkNotNull(treeNodeRenderer);
    }

    public void handleNodeRenderingChanged(NodeRenderingChanged<U> nodeRenderingChanged) {
        Optional<TreeNodeView<U>> view = viewMapper.getViewIfPresent(nodeRenderingChanged.getTreeNodeId());
        if (view.isPresent()) {
            String rendering = treeNodeRenderer.getHtmlRendering(nodeRenderingChanged.getUserObject());
            view.get().setRendering(rendering);
        }
    }
}
