package edu.stanford.protege.gwt.graphtree.client;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public interface TreeNodeViewActionHandler<U extends Serializable> {

    void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> treeNodeView);
}
