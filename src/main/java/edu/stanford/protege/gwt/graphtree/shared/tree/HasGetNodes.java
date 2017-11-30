package edu.stanford.protege.gwt.graphtree.shared.tree;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/01/2014
 */
public interface HasGetNodes<U extends Serializable> {

    /**
     * Gets the tree nodes in this tree.
     * @param parentNode An optional parent node.  If present then the tree nodes that
     *                   are children of this parent node will be returned.  If absent
     *                   then the root nodes of this tree will be returned.
     * @param callback A callback for receiving the returned nodes.
     */
    void getNodes(@Nonnull Optional<TreeNodeId> parentNode,
                  @Nonnull GetTreeNodesCallback<U> callback);
}
