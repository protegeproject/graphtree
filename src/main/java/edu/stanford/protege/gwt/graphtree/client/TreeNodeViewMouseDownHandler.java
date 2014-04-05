package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/01/2014
 */
public interface TreeNodeViewMouseDownHandler extends EventHandler {

    void handleMouseDown(TreeNodeViewMouseDownEvent event);
}
