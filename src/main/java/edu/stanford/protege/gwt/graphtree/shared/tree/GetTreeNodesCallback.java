package edu.stanford.protege.gwt.graphtree.shared.tree;

import java.io.Serializable;
import java.util.List;

/**
* Author: Matthew Horridge<br>
* Stanford University<br>
* Bio-Medical Informatics Research Group<br>
* Date: 12/02/2014
*/
public interface GetTreeNodesCallback<U extends Serializable> {
    void handleNodes(List<TreeNodeData<U>> nodes);
}
