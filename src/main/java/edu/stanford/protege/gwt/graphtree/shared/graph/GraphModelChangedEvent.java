package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;

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

    @JsonCreator
    public GraphModelChangedEvent(@JsonProperty("changes") Iterable<GraphModelChange<U>> changes) {
        this.changes = ImmutableList.copyOf(changes);
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

    @JsonIgnore
    @Override
    @SuppressWarnings("unchecked")
    public Type<GraphModelChangedHandler<U>> getAssociatedType() {
        return (Type<GraphModelChangedHandler<U>>) TYPE;
    }

    @Override
    protected void dispatch(GraphModelChangedHandler<U> handler) {
        handler.handleGraphModelChanged(this);
    }

    @JsonIgnore
    @Override
    public Object getSource() {
        return super.getSource();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GraphModelChangedEvent)) {
            return false;
        }
        GraphModelChangedEvent<?> that = (GraphModelChangedEvent<?>) o;
        return changes.equals(that.changes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(changes);
    }
}
