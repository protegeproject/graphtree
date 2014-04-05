package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.gwt.event.shared.HandlerRegistration;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/02/2014
 */
public class RemoteGraphModel<U extends Serializable> implements GraphModel<U> {

    @Override
    public void getKeyNodes(GetKeyNodesCallback<U> callback) {

    }

    @Override
    public void getSuccessorNodes(U userObject, GetSuccessorNodesCallback<U> callback) {
    }

//    @Override
//    public void getSuccessorNodes(Collection<U> userObjects, GetSuccessorNodesCallback<U> callback) {
//    }

    @Override
    public void getPathsFromKeyNodes(U toUserObject, GetPathsBetweenNodesCallback<U> paths) {
    }

    @Override
    public void getPathsBetweenNodes(U fromUserObject, U toUserObject, GetPathsBetweenNodesCallback<U> paths) {
    }

    @Override
    public HandlerRegistration addGraphModelHandler(GraphModelChangedHandler handler) {
        return null;
    }

}
