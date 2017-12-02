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
                    event.preventDefault();
                    selectPreviousTreeNodesHandler.invoke(TreeViewInputEvent.fromEvent(event), selectionProvider);
                    break;
                case KeyCodes.KEY_DOWN:
                    event.preventDefault();
                    selectNextNodesAction.invoke(TreeViewInputEvent.fromEvent(event), selectionProvider);
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
}
