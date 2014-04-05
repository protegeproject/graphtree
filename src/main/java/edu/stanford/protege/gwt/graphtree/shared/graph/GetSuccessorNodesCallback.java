package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.Multimap;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public interface GetSuccessorNodesCallback<U extends Serializable> {

    void handleSuccessorNodes(SuccessorMap<U> successorMap);
}
