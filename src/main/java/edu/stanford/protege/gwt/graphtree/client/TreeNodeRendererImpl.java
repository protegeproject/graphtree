package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/05/15
 */
public class TreeNodeRendererImpl<U extends Serializable> implements TreeNodeRenderer<U> {
    @Override
    public String getHtmlRendering(TreeNodeData<U> treeNode) {
        return treeNode.getShortForm();
    }
}
