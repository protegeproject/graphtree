package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.common.collect.ImmutableList;
import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public final class TreeNodeModelEvent extends GwtEvent<TreeNodeModelEventHandler> {

    private static final Type<TreeNodeModelEventHandler> TYPE = new Type<TreeNodeModelEventHandler>();

    private ImmutableList<TreeNodeModelChange> changes;

    public static Type<TreeNodeModelEventHandler> getType() {
        return TYPE;
    }

    private TreeNodeModelEvent() {
    }

    public TreeNodeModelEvent(List<TreeNodeModelChange> changes) {
        this.changes = ImmutableList.copyOf(changes);
    }



    @Override
    public Type<TreeNodeModelEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TreeNodeModelEventHandler handler) {
        handler.handleTreeNodeModelEvent(this);
    }

    public ImmutableList<TreeNodeModelChange> getChanges() {
        return changes;
    }

}
