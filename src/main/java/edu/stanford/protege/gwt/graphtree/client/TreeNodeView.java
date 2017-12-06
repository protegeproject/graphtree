package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 * <p>
 *     Represents a view of a {@link TreeNode}.  A {@link TreeNodeView} contains
 *     the rendering of the associated tree node and also holds {@link TreeNodeView}s
 *     corresponding the views of the child nodes.
 * </p>
 */
public interface TreeNodeView<U extends Serializable> extends IsWidget {

    /**
     * Gets the {@link TreeNode} that this view presents.
     * @return The {@link TreeNode} wrapped by this view.  Not {@code null}.
     */
    @Nonnull
    TreeNodeId getNodeId();

    /**
     * Gets the user object that this tree node view contains.
     * @return The user object.
     */
    @Nonnull
    U getUserObject();

    /**
     * Sets the user objec that this view contains
     */
    void setUserObject(@Nonnull U userObject);

    /**
     * Gets the tree node that this view displays.
     * @return The node
     */
    @Nonnull
    TreeNode<U> getNode();

    /**
     * Gets this view's parent view if it is present.
     * @return The optional {@link TreeNodeView} that corresponds to this view's
     * parent view.  Not {@code null}.  If this view does not have a parent view then
     * {@link Optional#empty()} will be returned.
     */
    Optional<TreeNodeView<U>> getParentView();

    Optional<TreeNodeView<U>> getPreviousSibling();

    Optional<TreeNodeView<U>> getNextSibling();

    void setPreviousSibling(TreeNodeView<U> previousSibling);

    void setNextSibling(TreeNodeView<U> nextSibling);

    Iterable<TreeNodeView<U>> getChildViews();

    int getChildViewCount();

    void setChildViews(List<TreeNodeView<U>> childViews);

    Optional<TreeNodeView<U>> getFirstChildView();

    Optional<TreeNodeView<U>> getLastChildView();

    void addChildView(TreeNodeView<U> child);

    void removeChildView(TreeNodeView<U> child, Runnable callback);

    boolean isExpanded();

    void setExpanded();

    boolean isCollapsed();

    void setCollapsed();

    TreeNodeViewState getViewState();

    void setViewState(TreeNodeViewState state);

    DataState getDataState();

    void setDataState(DataState state);

    void setLoadingIndicatorDisplayed(boolean displayed);

    void setRendering(String html);

    boolean isEmpty();

    void setSelected(boolean sel);

    void setLeaf(boolean isLeaf);

    int getDepth();

    void setDepth(int depth);

    void scrollIntoView();

    void setDragOver(boolean b);

    IsWidget getDragWidget();

    void setHidden(boolean hidden);

    void setPruned(boolean b);
}
