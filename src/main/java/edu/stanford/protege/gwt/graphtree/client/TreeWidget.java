package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeModel;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
@SuppressWarnings("unused")
public class TreeWidget<U extends Serializable, K> extends Composite implements HasAllMouseHandlers, HasContextMenuHandlers {

    private final TreePresenter<U, K> treePresenter;

    private final TreeView treeView;

    public TreeWidget(@Nonnull TreeView treeView,
                      @Nonnull TreeNodeModel<U, K> model,
                      @Nonnull TreeNodeRenderer<U> renderer) {
        this.treeView = checkNotNull(treeView);
        SingleSelectionModel<TreeNode<U>> selectionModel = new SingleSelectionModel<>();
        treePresenter = new TreePresenter<>(treeView, selectionModel, checkNotNull(renderer));
        treePresenter.setModel(checkNotNull(model));
        initWidget(treeView.asWidget());
    }

    public TreeWidget(@Nonnull TreeNodeModel<U, K> model) {
        this(model, new TreeNodeRendererImpl<>());
    }

    public TreeWidget(@Nonnull TreeNodeModel<U, K> model,
                      @Nonnull TreeNodeRenderer<U> renderer) {
        this(new TreeViewImpl(), model, renderer);
    }

    public TreeWidget(@Nonnull TreeNodeRenderer<U> renderer) {
        this(new TreeViewImpl(), new NullTreeNodeModel<>(), renderer);
    }

    /**
     * Creates a {@link TreeWidget} with the default view, model and renderer.
     */
    @Inject
    public TreeWidget() {
        this(new TreeViewImpl(), new NullTreeNodeModel<>(), new TreeNodeRendererImpl<>());
    }

    public void reload() {
        treePresenter.reload();
    }

    public void setModel(@Nonnull TreeNodeModel<U, K> model) {
        treePresenter.setModel(model);
    }

    public void setRenderer(@Nonnull TreeNodeRenderer<U> renderer) {
        treePresenter.setRenderer(renderer);
    }

    public void setRootNodesExpanded() {
        treePresenter.setRootNodesExpanded();
    }

    /**
     * Enables drag and drop.  The specified handler will receive drop callbacks.
     * @param dropHandler The drop handler to handle drop callbacks.
     */
    public void setDropHandler(@Nonnull TreeNodeDropHandler<U> dropHandler) {
        treePresenter.setDropHandler(dropHandler);
    }

    /**
     * Disables drag and drop.
     */
    public void clearDropHandler() {
        treePresenter.clearDropHandler();
    }

    @Nonnull
    public HandlerRegistration addSelectionChangeHandler(@Nonnull SelectionChangeEvent.Handler handler) {
        return treePresenter.addSelectionChangeHandler(handler);
    }

    @Nonnull
    public Path<TreeNodeId> getPathToRoot(@Nonnull TreeNodeId fromNode) {
        return treePresenter.getPathToRoot(fromNode);
    }

    public void clearSelection() {
        treePresenter.clearSelection();
    }

    @Nonnull
    public Set<TreeNode<U>> getSelectedNodes() {
        return treePresenter.getSelectedNodes();
    }

    @Nonnull
    public Set<K> getSelectedKeys() {
        return treePresenter.getSelectedKeys();
    }

    @Nonnull
    public Collection<Path<K>> getSelectedKeyPaths() {
        return treePresenter.getSelectedKeyPaths();
    }

    @Nonnull
    public Optional<K> getFirstSelectedKey() {
        return treePresenter.getFirstSelectedKey();
    }

    public boolean isSelected(@Nonnull TreeNode<U> object) {
        return treePresenter.isSelected(object);
    }

    public boolean isSelected(@Nonnull K key) {
        return treePresenter.isSelected(key);
    }

    public void setSelected(@Nonnull TreeNode<U> object,
                            boolean selected) {
        treePresenter.setSelected(object, selected);
    }

    public void setSelected(@Nonnull Path<K> keyPath,
                            boolean selected,
                            @Nonnull Runnable callback) {
        treePresenter.setSelected(keyPath, selected, callback);
    }

    public void setExpanded(@Nonnull Path<K> keyPath) {
        treePresenter.setExpanded(keyPath);
    }

    public void clearPruning() {
        treePresenter.clearPruning();
    }

    public void pruneToNodes(@Nonnull Collection<TreeNodeId> treeNodes) {
        treePresenter.pruneToNodes(treeNodes);
    }

    public void pruneToSelectedNodes() {
        treePresenter.pruneToSelectedNodes();
    }

    public void pruneToNodesContainingKey(@Nonnull K key, @Nonnull Runnable runnable) {
        treePresenter.pruneToNodesContainingKey(key, runnable);
    }

    public void revealTreeNodesForKey(@Nonnull K userObjectKey,
                                      @Nonnull RevealMode revealMode) {
        treePresenter.revealTreeNodesForKey(userObjectKey, revealMode);
    }



    @Override
    public com.google.gwt.event.shared.HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return treeView.addMouseDownHandler(handler);
    }

    @Override
    public com.google.gwt.event.shared.HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return treeView.addMouseMoveHandler(handler);
    }

    @Override
    public com.google.gwt.event.shared.HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return treeView.addMouseOutHandler(handler);
    }

    @Override
    public com.google.gwt.event.shared.HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return treeView.addMouseOverHandler(handler);
    }

    @Override
    public com.google.gwt.event.shared.HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return treeView.addMouseUpHandler(handler);
    }

    @Override
    public com.google.gwt.event.shared.HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return treeView.addMouseWheelHandler(handler);
    }

    @Override
    public com.google.gwt.event.shared.HandlerRegistration addContextMenuHandler(ContextMenuHandler contextMenuHandler) {
        return treeView.addContextMenuHandler(contextMenuHandler);
    }
}
