package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Iterator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public class TreeViewImpl extends Composite implements TreeView {

    private FocusPanel delegate;

    public TreeViewImpl() {
        initWidget(delegate = new FocusPanel());
        delegate.addStyleName(TreeNodeViewResources.RESOURCES.style().tree());
    }

    @Override
    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
        return delegate.addDoubleClickHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return delegate.addKeyDownHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return delegate.addKeyPressHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return delegate.addKeyUpHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return delegate.addMouseDownHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return delegate.addMouseMoveHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return delegate.addMouseOutHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return delegate.addMouseOverHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return delegate.addMouseUpHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return delegate.addMouseWheelHandler(handler);
    }

    @Override
    public void add(Widget w) {
        delegate.add(w);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return delegate.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return delegate.remove(w);
    }
}
