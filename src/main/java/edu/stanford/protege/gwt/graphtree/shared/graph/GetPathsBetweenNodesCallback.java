package edu.stanford.protege.gwt.graphtree.shared.graph;

import edu.stanford.protege.gwt.graphtree.shared.Path;

import java.io.Serializable;
import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
public interface GetPathsBetweenNodesCallback<U extends Serializable> {

    void handlePaths(Collection<Path<GraphNode<U>>> paths);
}
