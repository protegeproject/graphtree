package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/01/2014
 */
public class TreeNodeViewMouseDownEvent extends GwtEvent<TreeNodeViewMouseDownHandler> {

    private static final Type<TreeNodeViewMouseDownHandler> TYPE = new Type<TreeNodeViewMouseDownHandler>();

    private final TreeNodeView view;

    private final MouseDownEvent event;

    public TreeNodeViewMouseDownEvent(TreeNodeView view, MouseDownEvent event) {
        setSource(view);
        this.view = view;
        this.event = event;
    }

    public TreeNodeView getTreeNodeView() {
        return view;
    }

    public MouseDownEvent getMouseDownEvent() {
        return event;
    }

    @Override
    public Type<TreeNodeViewMouseDownHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<TreeNodeViewMouseDownHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TreeNodeViewMouseDownHandler handler) {
        handler.handleMouseDown(this);
    }
}
