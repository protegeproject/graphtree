package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public interface TreeNodeModelEventHandler extends EventHandler {

    void handleTreeNodeModelEvent(TreeNodeModelEvent event);
}
