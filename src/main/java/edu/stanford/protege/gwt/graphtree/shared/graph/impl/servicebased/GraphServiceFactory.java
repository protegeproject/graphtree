package edu.stanford.protege.gwt.graphtree.shared.graph.impl.servicebased;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public interface GraphServiceFactory {

    GetKeyNodesAction createGetKeyNodesAction();

    GetSuccessorNodesAction createGetSuccessorNodesAction(Serializable userObject);
}
