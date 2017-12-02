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
            Optional<TreeNodeViewEventTarget<U>> view1 = eventTargetManager.getEventTarget(event);
            if (view1.isPresent()) {
                dragAndDropHandler.handleDragStart(event, view1.get().getView());
            }
        }, DragStartEvent.getType());
        view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            if (target.isPresent()) {
                dragAndDropHandler.handleDragEnter(event, target.get().getView());
            }
        }, DragEnterEvent.getType());
        view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            if (target.isPresent()) {
                dragAndDropHandler.handleDragOver(event, target.get().getView());
            }
        }, DragOverEvent.getType());
        view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            if (target.isPresent()) {
                dragAndDropHandler.handleDragLeave(event, target.get().getView());
            }
        }, DragLeaveEvent.getType());
        view.asWidget().addDomHandler(event -> {
            event.preventDefault();
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            if (target.isPresent()) {
                dragAndDropHandler.handleDrop(event, target.get().getView());
            }
        }, DropEvent.getType());
        view.asWidget().addDomHandler(dragAndDropHandler::handleDragEnd, DragEndEvent.getType());
    }
}
