package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.Path;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2013
 * <p>
 *     A client side data structure that represents a tree.  A tree consists of {@link TreeNode} objects.  Each
 *     {@link TreeNode} is identified by an id (see {@link TreeNode#getId()})
 *     and is unique to a particular tree model.
 * </p>
 */
public interface TreeNodeModel<U extends Serializable> extends HasGetNodes<U>, HasGetBranches<U>, HasGetTreeNodesForUserObject<U> {

    @Nonnull
    HandlerRegistration addTreeNodeModelEventHandler(@Nonnull TreeNodeModelEventHandler handler);

    @Nonnull
    Path<TreeNodeData<U>> getPathToRoot(@Nonnull TreeNodeId treeNodeId);

    /**
     * Disposes of this TreeNodeModel, cleaning up any listeners that the tree node model might have
     * attached to other objects.
     */
    void dispose();
}
