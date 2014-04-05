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

    private SetTreeNodeSelectedHandler<U> setTreeNodeSelectedHandler;

    private ToggleExpansionStateHandler<U> toggleExpansionStateAction;

    private TreeViewEventTargetFinder<U> eventTargetFinder;

    public MouseEventMapper(SetTreeNodeSelectedHandler<U> setTreeNodeSelectedHandler,
                            ToggleExpansionStateHandler<U> toggleExpansionStateAction,
                            TreeViewEventTargetFinder<U> eventTargetFinder) {
        this.setTreeNodeSelectedHandler = setTreeNodeSelectedHandler;
        this.toggleExpansionStateAction = toggleExpansionStateAction;
        this.eventTargetFinder = eventTargetFinder;
    }

    public void bind(TreeView hasHandlers) {
        hasHandlers.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                handleMouseDownEvent(event);
            }
        });
        hasHandlers.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(MouseUpEvent event) {
                handleMouseUpEvent(event);
            }
        });
        hasHandlers.addDoubleClickHandler(new DoubleClickHandler() {
            public void onDoubleClick(DoubleClickEvent event) {
                handleDoubleClickEvent(event);
            }
        });
    }


    private void handleMouseDownEvent(MouseDownEvent event) {
        Optional<TreeNodeViewEventTarget<U>> target = eventTargetFinder.getEventTarget(event);
        if (target.isPresent() && !target.get().isViewHandleTarget()) {
            setTreeNodeSelectedHandler.invoke(TreeViewInputEvent.<U>fromEvent(event),
                    Collections.singleton(target.get().getView()));
        }
    }

    private void handleMouseUpEvent(MouseUpEvent event) {
        Optional<TreeNodeViewEventTarget<U>> target = eventTargetFinder.getEventTarget(event);
        if (target.isPresent() && target.get().isViewHandleTarget()) {
            toggleExpansionStateAction.invoke(TreeViewInputEvent.<U>fromEvent(event),
                    Collections.singleton(target.get().getView()));
        }
    }

    private void handleDoubleClickEvent(DoubleClickEvent event) {
        Optional<TreeNodeViewEventTarget<U>> target = eventTargetFinder.getEventTarget(event);
        if (target.isPresent()) {
            toggleExpansionStateAction.invoke(TreeViewInputEvent.<U>fromEvent(event),
                    Collections.singleton(target.get().getView()));
        }
    }

}
