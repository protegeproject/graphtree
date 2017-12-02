package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public abstract class TreeNodeMouseDownHandler<U extends Serializable> implements MouseDownHandler {

    private final TreeViewEventTargetFinder<U> eventTargetFinder;

    private final TreeNodeViewActionHandler handler;

    public TreeNodeMouseDownHandler(@Nonnull TreeNodeViewActionHandler handler,
                                    @Nonnull TreeViewEventTargetFinder<U> eventTargetFinder) {
        this.handler = checkNotNull(handler);
        this.eventTargetFinder = checkNotNull(eventTargetFinder);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        Optional<TreeNodeViewEventTarget<U>> target = eventTargetFinder.getEventTarget(event);
        if (target.isPresent() && !target.get().isViewHandleTarget()) {
            handler.invoke(TreeViewInputEvent.fromEvent(event), Collections.singleton(target.get().getView()));
        }
    }
}
