package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DataTransfer;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import edu.stanford.protege.gwt.graphtree.shared.DropType;
import edu.stanford.protege.gwt.graphtree.shared.Path;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/01/2014
 */
public class TreeNodeViewDragAndDropHandler<U extends Serializable> implements HasTreeNodeDropHandler<U> {

    private static final String TEXT_TRANSFER_DATA_KEY = "Text";

    private enum DropEffect {
        MOVE("move"),
        ADD("add"),
        LINK("link"),
        NONE("none");

        final String dropEffect;

        DropEffect(String dropEffect) {
            this.dropEffect = dropEffect;
        }

        public String getDropEffect() {
            return dropEffect;
        }
    }

    private Optional<TreeNodeView<U>> draggedNode = Optional.empty();

    private TreeNodeDropHandler<U> treeNodeDropHandler;

    private final HasPendingChanges<U> hasPendingChanges;

    @Inject
    public TreeNodeViewDragAndDropHandler(HasPendingChanges<U> hasPendingChanges) {
        this.hasPendingChanges = hasPendingChanges;
        this.treeNodeDropHandler = new NoOpTreeNodeDropHandler<>();
    }

    public void setDropHandler(@Nonnull TreeNodeDropHandler<U> handler) {
        this.treeNodeDropHandler = checkNotNull(handler);
    }

    public void handleDragStart(DragStartEvent event, TreeNodeView<U> targetView) {
        draggedNode = Optional.of(targetView);
        setupDragImage(event, targetView);
        setupTransferData(event, targetView);
        updateDropEffect(event, targetView);
    }

    private void setupTransferData(DragStartEvent event, TreeNodeView<U> targetView) {
        DataTransfer dataTransfer = event.getDataTransfer();
        GWT.log("TRANSFER DATA HAS NOT BEEN SET UP!");
//        dataTransfer.setData(TEXT_TRANSFER_DATA_KEY, targetView.getNode());
    }

    private void setupDragImage(DragStartEvent event, TreeNodeView targetView) {
        IsWidget dragWidget = targetView.getDragWidget();
        NativeEvent nativeEvent = event.getNativeEvent();
        final int clientX = nativeEvent.getClientX();
        final int clientY = nativeEvent.getClientY();
        final Widget widget = dragWidget.asWidget();
        final int elementX = widget.getAbsoluteLeft();
        final int elementY = widget.getAbsoluteTop();
        DataTransfer dataTransfer = event.getDataTransfer();
        dataTransfer.setDragImage(targetView.getDragWidget().asWidget().getElement(),
                clientX - elementX,
                clientY - elementY);
    }

    public void handleDragEnter(DragEnterEvent event, TreeNodeView<U> targetView) {
        updateDropEffect(event, targetView);
    }

    public void handleDragOver(DragOverEvent event, TreeNodeView<U> targetView) {
        updateDropEffect(event, targetView);
    }

    public void handleDragLeave(DragLeaveEvent event, TreeNodeView<U> targetView) {
        updateDropEffect(event, targetView);
        targetView.setDragOver(false);
    }

    public void handleDrop(DropEvent event, final TreeNodeView<U> targetView) {
        targetView.setDragOver(false);
        if (draggedNode.isPresent()) {
            handleTreeNodeDrop(event, targetView);
        }
        else {
            handleTextDrop(event, targetView);
        }
    }

    private void handleTextDrop(DropEvent event, final TreeNodeView<U> targetView) {
        String text = event.getData(TEXT_TRANSFER_DATA_KEY);
        if (text == null) {
            return;
        }
        if (text.isEmpty()) {
            return;
        }
        hasPendingChanges.setChildAdditionPending(targetView);
        final Path<U> dropPath = TreeNodeViewTraverser.<U>newTreeNodeViewTraverser().getUserObjectPathToRoot
                (targetView);
        treeNodeDropHandler.handleTextDrop(text,
                dropPath,
                getDropType(event),
                new TreeNodeDropHandler.DropEndHandler() {
                    @Override
                    public void handleDropComplete() {
                    }

                    @Override
                    public void handleDropCancelled() {
                        hasPendingChanges.setPendingChangedCancelled(targetView);
                    }
                });
    }

    private void handleTreeNodeDrop(DropEvent event, final TreeNodeView<U> targetView) {
        draggedNode.ifPresent(draggedNode -> {
            final Path<U> draggedPath = getDraggedNodePath();
            final Path<U> dropPath = TreeNodeViewTraverser.<U>newTreeNodeViewTraverser().getUserObjectPathToRoot
                    (targetView);
            hasPendingChanges.setChildAdditionPending(targetView);
            if (getDnDConstant(event) == DropEffect.MOVE) {
                hasPendingChanges.setRemovalPending(draggedNode);
            }
            treeNodeDropHandler.handleDrop(draggedPath,
                                           dropPath,
                                           getDropType(event),
                                           new TreeNodeDropHandler.DropEndHandler() {
                                               public void handleDropComplete() {
                                                   clearDraggedTreeNode();
                                               }

                                               public void handleDropCancelled() {
                                                   GWT.log("[TreeNodeViewDragAndDropHandler] handling drop cancelled");
                                                   hasPendingChanges.setPendingChangedCancelled(targetView);
                                                   hasPendingChanges.setPendingChangedCancelled(draggedNode);
                                                   clearDraggedTreeNode();
                                               }
                                           });
        });
    }

    private Path<U> getDraggedNodePath() {
        return TreeNodeViewTraverser.<U>newTreeNodeViewTraverser().getUserObjectPathToRoot(draggedNode.get());
    }

    public void handleDragEnd(DragEndEvent event) {
    }

    private void clearDraggedTreeNode() {
        draggedNode = Optional.empty();
    }

    private void updateDropEffect(DragDropEventBase<?> event, TreeNodeView<U> targetView) {
        Path<U> pathToRoot = TreeNodeViewTraverser.<U>newTreeNodeViewTraverser().getUserObjectPathToRoot(targetView);
        if (isTreeNodeDrag(targetView) && treeNodeDropHandler.isDropPossible(getDraggedNodePath(),
                pathToRoot,
                getDropType(event))) {
            DropEffect effect = getDnDConstant(event);
            setDropEffect(event.getNativeEvent(), effect);
            targetView.setDragOver(true);
        }
        else if (isTextDrag(event) && treeNodeDropHandler.isTextDropPossible(pathToRoot,
                getDropType(event))) {
            setDropEffect(event.getNativeEvent(), getDnDConstant(event));
            targetView.setDragOver(true);
        }
        else {
            setDropEffect(event.getNativeEvent(), DropEffect.NONE);
            targetView.setDragOver(false);
        }
    }

    private boolean isTreeNodeDrag(TreeNodeView<U> targetView) {
        return draggedNode.isPresent() && !draggedNode.get().getNodeId().equals(targetView.getNodeId());
    }

    private boolean isTextDrag(DragDropEventBase<?> event) {
        return event.getData(TEXT_TRANSFER_DATA_KEY) != null;
    }

    private static DropEffect getDnDConstant(HasNativeEvent event) {
        DropType dropType = getDropType(event);
        if (dropType == DropType.ADD) {
            return DropEffect.ADD;
        }
        else {
            return DropEffect.MOVE;
        }
    }

    private static DropType getDropType(HasNativeEvent event) {
        if (event.getNativeEvent().getAltKey()) {
            return DropType.ADD;
        }
        else {
            return DropType.MOVE;
        }
    }

    private static void setDropEffect(NativeEvent e, DropEffect constant) {
        setDropEffect(e, constant.name());
    }

    /**
     * Sets the HTML 5 drop effect.  This can be used to update the cursor to indicate the kind of drop (e.g. a MOVE
     * or a copy) or it can be used to cancel a drop (by setting to "NONE").  Unfortunately, the GWT API does not expose
     * this functionality, hence the need for the JSNI method.
     *
     * @param e      The native event to set the effect on.
     * @param effect The effect to set. See {@link DropEffect}
     *               for the different types of effect.
     */
    private static native void setDropEffect(NativeEvent e, String effect) /*-{
        var dataTransfer = e.dataTransfer;
        dataTransfer.dropEffect = effect
    }-*/;
}
