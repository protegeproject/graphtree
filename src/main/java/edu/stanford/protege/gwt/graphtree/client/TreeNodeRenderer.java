package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/05/15
 */
public interface TreeNodeRenderer<U extends Serializable> {

    String getHtmlRendering(TreeNodeData<U> treeNode);
}
