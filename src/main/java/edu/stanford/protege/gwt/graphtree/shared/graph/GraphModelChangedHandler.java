package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.gwt.event.shared.EventHandler;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public interface GraphModelChangedHandler<U extends Serializable> extends EventHandler {

    void handleGraphModelChanged(GraphModelChangedEvent<U> event);
}
