package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.common.collect.Multimap;

import java.io.Serializable;
import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/01/2014
 */
public interface HasGetBranches<U extends Serializable> {

    void getBranchesContainingUserObject(U userObject, GetBranchesCallback<U> callback);

    public static interface  GetBranchesCallback<U extends Serializable> {
        void handleBranches(Multimap<TreeNodeData<U>, TreeNodeData<U>> parent2ChildMap);
    }
}
