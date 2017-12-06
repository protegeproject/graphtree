package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Iterator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public class TreeViewImpl<U extends Serializable> extends Composite implements TreeView<U> {

    private final FocusPanel focusPanel;

    private final FlowPanel delegate;

    public TreeViewImpl() {
        initWidget(focusPanel = new FocusPanel(delegate = new FlowPanel()));
        delegate.setSize("100%", "100%");
        focusPanel.addStyleName(TreeNodeViewResources.RESOURCES.style().tree());
    }

    @Override
    public int getTreeNodeViewCount() {
        return delegate.getWidgetCount();
    }

    @Override
    public int getIndexOf(TreeNodeView<U> view) {
        return delegate.getWidgetIndex(view);
    }

    @Override
    public TreeNodeView<U> getTreeNodeViewAt(int index) {
        return (TreeNodeView<U>) delegate.getWidget(index);
    }

    @Override
    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
        return focusPanel.addDoubleClickHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return focusPanel.addKeyDownHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return focusPanel.addKeyPressHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return focusPanel.addKeyUpHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return focusPanel.addMouseDownHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return focusPanel.addMouseMoveHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return focusPanel.addMouseOutHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return focusPanel.addMouseOverHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return focusPanel.addMouseUpHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return focusPanel.addMouseWheelHandler(handler);
    }

    @Override
    public HandlerRegistration addContextMenuHandler(ContextMenuHandler contextMenuHandler) {
        return focusPanel.addDomHandler(event -> {
            event.preventDefault();
            event.stopPropagation();
            contextMenuHandler.onContextMenu(event);
        }, ContextMenuEvent.getType());
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
    @Nonnull
    public Iterator<Widget> iterator() {
        return delegate.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return delegate.remove(w);
    }
}
