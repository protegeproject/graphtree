package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public interface TreeNodeViewMapper<U extends Serializable> {

    /**
     * Gets the {@link TreeNodeView} for the specified {@link TreeNodeId} if present.
     * @param treeNode The {@link TreeNodeId} whose view should be retrieved. Not {@code null}.
     * @return The optional {@link TreeNodeView} which is a view for the specified {@link TreeNodeId}.  Not {@code null}.
     * A value of {@link Optional#empty()} indicates that a view for the specified node does not exist.
     */
    Optional<TreeNodeView<U>> getViewIfPresent(TreeNodeId treeNode);
}
