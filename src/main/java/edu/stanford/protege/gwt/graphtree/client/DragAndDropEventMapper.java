package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.gwt.event.dom.client.*;

import java.io.Serializable;

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
        view.asWidget().addDomHandler(new DragStartHandler() {
            public void onDragStart(DragStartEvent event) {
                Optional<TreeNodeViewEventTarget<U>> view = eventTargetManager.getEventTarget(event);
                if (view.isPresent()) {
                    dragAndDropHandler.handleDragStart(event, view.get().getView());
                }
            }
        }, DragStartEvent.getType());
        view.asWidget().addDomHandler(new DragEnterHandler() {
            public void onDragEnter(DragEnterEvent event) {
                Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
                if (target.isPresent()) {
                    dragAndDropHandler.handleDragEnter(event, target.get().getView());
                }
            }
        }, DragEnterEvent.getType());
        view.asWidget().addDomHandler(new DragOverHandler() {
            public void onDragOver(DragOverEvent event) {
                Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
                if (target.isPresent()) {
                    dragAndDropHandler.handleDragOver(event, target.get().getView());
                }
            }
        }, DragOverEvent.getType());
        view.asWidget().addDomHandler(new DragLeaveHandler() {
            public void onDragLeave(DragLeaveEvent event) {
                Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
                if (target.isPresent()) {
                    dragAndDropHandler.handleDragLeave(event, target.get().getView());
                }
            }
        }, DragLeaveEvent.getType());
        view.asWidget().addDomHandler(new DropHandler() {
            public void onDrop(DropEvent event) {
                event.preventDefault();
                Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
                if (target.isPresent()) {
                    dragAndDropHandler.handleDrop(event, target.get().getView());
                }
            }
        }, DropEvent.getType());
        view.asWidget().addDomHandler(new DragEndHandler() {
            public void onDragEnd(DragEndEvent event) {
                dragAndDropHandler.handleDragEnd(event);
            }
        }, DragEndEvent.getType());
    }
}
