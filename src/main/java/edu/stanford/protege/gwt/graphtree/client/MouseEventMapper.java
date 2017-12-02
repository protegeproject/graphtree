package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.gwt.event.dom.client.*;

import java.io.Serializable;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2014
 */
public class MouseEventMapper<U extends Serializable> {

    private final SetTreeNodeSelectedHandler<U> setTreeNodeSelectedHandler;

    private final ToggleExpansionStateHandler<U> toggleExpansionStateAction;

    private final TreeViewEventTargetFinder<U> eventTargetFinder;

    public MouseEventMapper(SetTreeNodeSelectedHandler<U> setTreeNodeSelectedHandler,
                            ToggleExpansionStateHandler<U> toggleExpansionStateAction,
                            TreeViewEventTargetFinder<U> eventTargetFinder) {
        this.setTreeNodeSelectedHandler = setTreeNodeSelectedHandler;
        this.toggleExpansionStateAction = toggleExpansionStateAction;
        this.eventTargetFinder = eventTargetFinder;
    }

    public void bind(TreeView hasHandlers) {
        hasHandlers.addMouseDownHandler(this::handleMouseDownEvent);
        hasHandlers.addMouseUpHandler(this::handleMouseUpEvent);
        hasHandlers.addDoubleClickHandler(this::handleDoubleClickEvent);
    }


    private void handleMouseDownEvent(MouseDownEvent event) {
        Optional<TreeNodeViewEventTarget<U>> target = eventTargetFinder.getEventTarget(event);
        if (target.isPresent() && !target.get().isViewHandleTarget()) {
            setTreeNodeSelectedHandler.invoke(TreeViewInputEvent.fromEvent(event),
                    Collections.singleton(target.get().getView()));
        }
    }

    private void handleMouseUpEvent(MouseUpEvent event) {
        Optional<TreeNodeViewEventTarget<U>> target = eventTargetFinder.getEventTarget(event);
        if (target.isPresent() && target.get().isViewHandleTarget()) {
            toggleExpansionStateAction.invoke(TreeViewInputEvent.fromEvent(event),
                    Collections.singleton(target.get().getView()));
        }
    }

    private void handleDoubleClickEvent(DoubleClickEvent event) {
        Optional<TreeNodeViewEventTarget<U>> target = eventTargetFinder.getEventTarget(event);
        if (target.isPresent()) {
            toggleExpansionStateAction.invoke(TreeViewInputEvent.fromEvent(event),
                    Collections.singleton(target.get().getView()));
        }
    }

}
