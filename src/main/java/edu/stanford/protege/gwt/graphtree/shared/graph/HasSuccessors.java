package edu.stanford.protege.gwt.graphtree.shared.graph;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/01/2014
 */
public interface HasSuccessors<N> {

    Iterable<N> getSuccessors(N node);
}
