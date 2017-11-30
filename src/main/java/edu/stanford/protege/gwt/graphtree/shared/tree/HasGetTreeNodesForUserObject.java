package edu.stanford.protege.gwt.graphtree.shared.tree;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/02/2014
 */
public interface HasGetTreeNodesForUserObject<U extends Serializable> {

    /**
     * Gets the tree nodes for the specified user object.  All of the tree nodes that
     * represent the user object will be returned, whether or not they have been explicitly
     * revealed by the user.
     * @param userObject The user object.
     * @param callback A callback for receiving the nodes asynchronously.
     */
    void getTreeNodesForUserObject(@Nonnull U userObject,
                                   @Nonnull GetTreeNodesCallback<U> callback);
}
