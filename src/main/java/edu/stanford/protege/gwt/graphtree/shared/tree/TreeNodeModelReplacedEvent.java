package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/02/2014
 */
public class TreeNodeModelReplacedEvent extends GwtEvent<TreeNodeModelReplacedHandler> {

    private static final Type<TreeNodeModelReplacedHandler> TYPE = new Type<TreeNodeModelReplacedHandler>();

    @Override
    public Type<TreeNodeModelReplacedHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<TreeNodeModelReplacedHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TreeNodeModelReplacedHandler handler) {
        handler.handleTreeModelReplaced(this);
    }
}
