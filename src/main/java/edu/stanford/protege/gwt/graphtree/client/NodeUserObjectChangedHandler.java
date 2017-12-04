package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.tree.NodeUserObjectChanged;

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
public class NodeUserObjectChangedHandler<U extends Serializable> {

    private final TreeNodeViewManager<U> viewMapper;

    public NodeUserObjectChangedHandler(@Nonnull TreeNodeViewManager<U> viewMapper) {
        this.viewMapper = checkNotNull(viewMapper);
    }

    public void handleNodeUserObjectChanged(NodeUserObjectChanged<U> nodeUserObjectChanged) {
        Optional<TreeNodeView<U>> view = viewMapper.getViewIfPresent(nodeUserObjectChanged.getTreeNodeId());
        view.ifPresent(v -> {
            String rendering = viewMapper.getRenderer().getHtmlRendering(nodeUserObjectChanged.getUserObject());
            v.setRendering(rendering);
            v.setUserObject(nodeUserObjectChanged.getUserObject());
        });
    }
}
