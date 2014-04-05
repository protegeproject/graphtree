package edu.stanford.protege.gwt.graphtree.shared.graph;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public interface GetKeyNodesCallback<U extends Serializable> {

    void handleKeyNodes(List<GraphNode<U>> roots);
}
