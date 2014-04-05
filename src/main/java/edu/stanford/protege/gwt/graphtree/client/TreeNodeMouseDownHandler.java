package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;

import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public abstract class TreeNodeMouseDownHandler implements MouseDownHandler {

    private TreeViewEventTargetFinder eventTargetFinder;

    private TreeNodeViewActionHandler handler;

    public TreeNodeMouseDownHandler(TreeNodeViewActionHandler handler, TreeViewEventTargetFinder eventTargetFinder) {
        this.handler = handler;
        this.eventTargetFinder = eventTargetFinder;
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        Optional<TreeNodeViewEventTarget> target = eventTargetFinder.getEventTarget(event);
        if (target.isPresent() && !target.get().isViewHandleTarget()) {
            handler.invoke(TreeViewInputEvent.fromEvent(event), Collections.singleton(target.get().getView()));
        }
    }
}
