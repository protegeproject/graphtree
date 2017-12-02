package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.*;

import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public class DragAndDropEventMapper<U extends Serializable> implements HasTreeNodeDropHandler<U> {

    private final TreeViewEventTargetFinder<U> eventTargetManager;

    private final TreeNodeViewDragAndDropHandler<U> dragAndDropHandler;

    public DragAndDropEventMapper(TreeViewEventTargetFinder<U> eventTargetManager,
                                  TreeNodeViewDragAndDropHandler<U> dragAndDropHandler) {
        this.eventTargetManager = eventTargetManager;
        this.dragAndDropHandler = dragAndDropHandler;
    }


    public void setDropHandler(TreeNodeDropHandler<U> dropHandler) {
        dragAndDropHandler.setDropHandler(dropHandler);
    }

    public void bind(TreeView view) {
        view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            target.ifPresent(taretView -> dragAndDropHandler.handleDragStart(event, taretView.getView()));
        }, DragStartEvent.getType());

        view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            target.ifPresent(targetView -> dragAndDropHandler.handleDragEnter(event, targetView.getView()));
        }, DragEnterEvent.getType());

        view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            target.ifPresent(targetView -> dragAndDropHandler.handleDragOver(event, targetView.getView()));
        }, DragOverEvent.getType());

        view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            target.ifPresent(targetView -> dragAndDropHandler.handleDragLeave(event, targetView.getView()));
        }, DragLeaveEvent.getType());

        view.asWidget().addDomHandler(event -> {
            event.preventDefault();
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            target.ifPresent(targetView -> dragAndDropHandler.handleDrop(event, targetView.getView()));
        }, DropEvent.getType());

        view.asWidget().addDomHandler(dragAndDropHandler::handleDragEnd, DragEndEvent.getType());
    }
}
