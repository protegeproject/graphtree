package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/02/2014
 */
public interface TreeNodeModelReplacedHandler extends EventHandler {

    void handleTreeModelReplaced(TreeNodeModelReplacedEvent event);
}
