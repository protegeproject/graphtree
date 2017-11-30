package edu.stanford.protege.gwt.graphtree.shared.tree;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/02/2014
 */
public interface HasGetTreeNodesForUserObjectKey<U extends Serializable, K> {

    /**
     * Gets the tree nodes for the specified user object key.  All of the tree nodes that
     * represent the user object with the specified key will be returned, whether or not they have been explicitly
     * revealed by the user.
     * @param userObjectKey The user object key.
     * @param callback A callback for receiving the nodes asynchronously.
     */
    void getTreeNodesForUserObjectKey(@Nonnull K userObjectKey,
                                   @Nonnull GetTreeNodesCallback<U> callback);
}
