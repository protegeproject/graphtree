package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.*;
import com.google.web.bindery.event.shared.HandlerRegistration;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public class DragAndDropEventMapper<U extends Serializable> implements HasTreeNodeDropHandler<U> {

    private final TreeView view;

    private final TreeViewEventTargetFinder<U> eventTargetManager;

    private final TreeNodeViewDragAndDropHandler<U> dragAndDropHandler;

    private final List<HandlerRegistration> handlerRegistrations = new ArrayList<>();

    public DragAndDropEventMapper(@Nonnull TreeView view,
                                  @Nonnull TreeViewEventTargetFinder<U> eventTargetManager,
                                  @Nonnull TreeNodeViewDragAndDropHandler<U> dragAndDropHandler) {
        this.view = checkNotNull(view);
        this.eventTargetManager = checkNotNull(eventTargetManager);
        this.dragAndDropHandler = checkNotNull(dragAndDropHandler);
    }

    public void setDropHandler(@Nonnull TreeNodeDropHandler<U> dropHandler) {
        dragAndDropHandler.setDropHandler(dropHandler);
        registerHandlers();
    }

    public void clearDropHandler() {
        handlerRegistrations.forEach(HandlerRegistration::removeHandler);
        handlerRegistrations.clear();
    }

    private void registerHandlers() {
        handlerRegistrations.add(registerDragStartHandler());
        handlerRegistrations.add(registerDragEnterHandler());
        handlerRegistrations.add(registerDragOverHandler());
        handlerRegistrations.add(registerDragLeaveHandler());
        handlerRegistrations.add(registerDropHandler());
        handlerRegistrations.add(registerDragEndHandler());
    }

    private HandlerRegistration registerDragEndHandler() {
        return view.asWidget().addDomHandler(dragAndDropHandler::handleDragEnd, DragEndEvent.getType());
    }

    private HandlerRegistration registerDropHandler() {
        return view.asWidget().addDomHandler(event -> {
            event.preventDefault();
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            target.ifPresent(targetView -> dragAndDropHandler.handleDrop(event, targetView.getView()));
        }, DropEvent.getType());
    }

    private HandlerRegistration registerDragLeaveHandler() {
        return view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            target.ifPresent(targetView -> dragAndDropHandler.handleDragLeave(event, targetView.getView()));
        }, DragLeaveEvent.getType());
    }

    private HandlerRegistration registerDragOverHandler() {
        return view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            target.ifPresent(targetView -> dragAndDropHandler.handleDragOver(event, targetView.getView()));
        }, DragOverEvent.getType());
    }

    private HandlerRegistration registerDragEnterHandler() {
        return view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            target.ifPresent(targetView -> dragAndDropHandler.handleDragEnter(event, targetView.getView()));
        }, DragEnterEvent.getType());
    }

    private HandlerRegistration registerDragStartHandler() {
        return view.asWidget().addDomHandler(event -> {
            Optional<TreeNodeViewEventTarget<U>> target = eventTargetManager.getEventTarget(event);
            target.ifPresent(taretView -> dragAndDropHandler.handleDragStart(event, taretView.getView()));
        }, DragStartEvent.getType());
    }
}
