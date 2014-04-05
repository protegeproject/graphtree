package edu.stanford.protege.gwt.graphtree.shared.tree;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/02/2014
 */
public interface HasGetTreeNodesForUserObject<U extends Serializable> {

    void getTreeNodesForUserObject(U userObject, GetTreeNodesCallback<U> callback);
}
