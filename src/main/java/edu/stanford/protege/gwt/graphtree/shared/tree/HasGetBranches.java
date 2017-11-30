package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.common.collect.Multimap;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/01/2014
 */
public interface HasGetBranches<U extends Serializable> {

    /**
     * Gets the branches that contain the specified user object.
     * @param userObject The user object.
     * @param callback A callback for receiving the branches.
     */
    void getBranchesContainingUserObject(@Nonnull U userObject,
                                         @Nonnull GetBranchesCallback<U> callback);

    interface  GetBranchesCallback<U extends Serializable> {
        void handleBranches(Multimap<TreeNodeData<U>, TreeNodeData<U>> parent2ChildMap);
    }
}
