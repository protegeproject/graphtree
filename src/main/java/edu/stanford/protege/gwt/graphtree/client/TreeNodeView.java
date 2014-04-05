package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 * <p>
 *     Represents a view of a {@link edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode}.  A {@link TreeNodeView} contains
 *     the rendering of the associated tree node and also holds {@link edu.stanford.protege.gwt.graphtree.client.TreeNodeView}s
 *     corresponding the views of the child nodes.
 * </p>
 */
public interface TreeNodeView<U extends Serializable> extends IsWidget {

    /**
     * Gets the {@link edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode} that this view presents.
     * @return The {@link edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode} wrapped by this view.  Not {@code null}.
     */
    TreeNodeId getNodeId();

    U getUserObject();

    TreeNode<U> getNode();

//    /**
//     * Sets the {@link edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId} that this view presents.
//     * @param node The {@link edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId}.  Not {@code null}.
//     * @throws java.lang.NullPointerException if {@code node} is {@code null}.
//     */
//    void setNode(TreeNode node);

    /**
     * Gets this view's parent view if it is present.
     * @return The optional {@link TreeNodeView} that corresponds to this view's
     * parent view.  Not {@code null}.  If this view does not have a parent view then
     * {@link com.google.common.base.Optional#absent()} will be returned.
     */
    Optional<TreeNodeView<U>> getParentView();

    Optional<TreeNodeView<U>> getPreviousSibling();

    Optional<TreeNodeView<U>> getNextSibling();

    Iterable<TreeNodeView<U>> getChildViews();

    void setChildViews(List<TreeNodeView<U>> childViews);

    Optional<TreeNodeView<U>> getFirstChildView();

    Optional<TreeNodeView<U>> getLastChildView();

    void addChildView(TreeNodeView<U> child);

    void removeChildView(TreeNodeView<U> child);

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
}
