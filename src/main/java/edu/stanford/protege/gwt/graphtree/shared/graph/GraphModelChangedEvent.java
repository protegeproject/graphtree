package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.ImmutableList;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import java.io.Serializable;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public final class GraphModelChangedEvent<U extends Serializable> extends GwtEvent<GraphModelChangedHandler<U>> implements Serializable {

    private static transient final Type<?> TYPE = new Type();

    private ImmutableList<GraphModelChange<U>> changes;

    private GraphModelChangedEvent() {
    }

    public GraphModelChangedEvent(Iterable<GraphModelChange<U>> changes) {
        this.changes = ImmutableList.<GraphModelChange<U>>copyOf(changes);
    }

    @SuppressWarnings("unchecked")
    public static <U extends Serializable> Type<GraphModelChangedHandler<U>> getType() {
        return (Type<GraphModelChangedHandler<U>>) TYPE;
    }


    public static <U extends Serializable> void fire(HasHandlers source, Iterable<GraphModelChange<U>> changes) {
        source.fireEvent(new GraphModelChangedEvent<>(changes));
    }

    public static <U extends Serializable> void fire(HasHandlers source, GraphModelChange<U> change) {
        source.fireEvent(new GraphModelChangedEvent<>(Collections.singleton(change)));
    }

    public ImmutableList<GraphModelChange<U>> getChanges() {
        return changes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<GraphModelChangedHandler<U>> getAssociatedType() {
        return (Type<GraphModelChangedHandler<U>>) TYPE;
    }

    @Override
    protected void dispatch(GraphModelChangedHandler<U> handler) {
        handler.handleGraphModelChanged(this);
    }
}
