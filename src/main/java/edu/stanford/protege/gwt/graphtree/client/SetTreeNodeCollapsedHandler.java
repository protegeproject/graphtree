package edu.stanford.protege.gwt.graphtree.client;

import com.google.inject.Inject;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class SetTreeNodeCollapsedHandler<U extends Serializable> implements TreeNodeViewActionHandler<U> {


    @Inject
    public SetTreeNodeCollapsedHandler() {
    }

    public void invoke(TreeViewInputEvent<U> event, Iterable<TreeNodeView<U>> views) {
        for (TreeNodeView view : views) {
            view.setCollapsed();
        }
    }
}
