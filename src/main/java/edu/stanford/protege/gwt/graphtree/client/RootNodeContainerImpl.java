package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Iterator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/02/2014
 */
public class RootNodeContainerImpl implements RootNodeContainer {

    private final FlowPanel delegate = new FlowPanel();

    public void add(Widget w) {
        delegate.add(w);
    }

    public void clear() {
        delegate.clear();
    }

    public Iterator<Widget> iterator() {
        return delegate.iterator();
    }

    public boolean remove(Widget w) {
        return delegate.remove(w);
    }

    public Widget asWidget() {
        return delegate.asWidget();
    }
}
