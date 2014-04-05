package edu.stanford.protege.gwt.graphtree.client;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2014
 */
public interface HasPendingChanges<U extends Serializable> {

    void setChildAdditionPending(TreeNodeView<U> parentView);

    void setRemovalPending(TreeNodeView<U> removedView);

    void setRendingChangePending(TreeNodeView<U> view);

    void setPendingChangedCancelled(TreeNodeView<U> view);
}
