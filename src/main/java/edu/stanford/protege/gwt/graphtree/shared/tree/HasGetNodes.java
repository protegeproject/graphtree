package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/01/2014
 */
public interface HasGetNodes<U extends Serializable> {

    void getNodes(Optional<TreeNodeId> parentNode, GetTreeNodesCallback<U> callback);
}
