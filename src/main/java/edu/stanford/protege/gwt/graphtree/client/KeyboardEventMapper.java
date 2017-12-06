package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.*;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class KeyboardEventMapper<U extends Serializable> {

    private final SelectPreviousTreeNodesHandler<U> selectPreviousTreeNodesHandler;

    private final SelectNextTreeNodesHandler<U> selectNextNodesAction;

    private final SetTreeNodeCollapsedHandler<U> setNodeCollapsedAction;

    private final SetTreeNodeExpandedHandler<U> setNodeExpandedAction;

    private final TreeNodeViewSelectionProvider<U> selectionProvider;

    public KeyboardEventMapper(TreeNodeViewSelectionProvider<U> selectionProvider,
                               SetTreeNodeExpandedHandler<U> setNodeExpandedAction,
                               SetTreeNodeCollapsedHandler<U> setNodeCollapsedAction,
                               SelectNextTreeNodesHandler<U> selectNextNodesAction,
                               SelectPreviousTreeNodesHandler<U> selectPreviousTreeNodesHandler) {
        this.selectionProvider = selectionProvider;
        this.setNodeExpandedAction = setNodeExpandedAction;
        this.setNodeCollapsedAction = setNodeCollapsedAction;
        this.selectNextNodesAction = selectNextNodesAction;
        this.selectPreviousTreeNodesHandler = selectPreviousTreeNodesHandler;
    }

    public void bind(TreeView view) {
        view.addKeyDownHandler(event -> {
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_UP:
                    moveSelectionUp(event);
                    break;
                case KeyCodes.KEY_DOWN:
                    moveSelectionDown(event);
                    break;
                case KeyCodes.KEY_LEFT:
                    event.preventDefault();
                    setNodeCollapsedAction.invoke(TreeViewInputEvent.fromEvent(event), selectionProvider);
                    break;
                case KeyCodes.KEY_RIGHT:
                    event.preventDefault();
                    setNodeExpandedAction.invoke(TreeViewInputEvent.fromEvent(event), selectionProvider);
                    break;
            }
        });
    }

    public void moveSelectionUp() {
        moveSelectionUp(TreeViewInputEvent.empty());
    }

    public void moveSelectionUp(TreeViewInputEvent<U> event) {
        selectPreviousTreeNodesHandler.invoke(event, selectionProvider);
    }

    private void moveSelectionUp(KeyDownEvent event) {
        event.preventDefault();
        moveSelectionUp(TreeViewInputEvent.fromEvent(event));
    }

    public void moveSelectionDown() {
        moveSelectionDown(TreeViewInputEvent.empty());
    }

    private void moveSelectionDown(KeyDownEvent event) {
        event.preventDefault();
        moveSelectionDown(TreeViewInputEvent.fromEvent(event));
    }

    public void moveSelectionDown(TreeViewInputEvent<U> event) {
        selectNextNodesAction.invoke(event, selectionProvider);
    }


}
