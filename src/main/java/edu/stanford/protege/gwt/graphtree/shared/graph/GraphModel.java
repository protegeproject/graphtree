package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.gwt.event.shared.HandlerRegistration;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/07/2013
 * <p>
 *     The basic interface to a graph structure - a set of nodes and successor relationships.  The user object of a
 *     graph node uniquely determines it.
 * </p>
 */
public interface GraphModel<U extends Serializable> {

    /**
     * Gets the "key" nodes of the graph.  These are similar to source nodes in a directed graph except that they may
     * have incoming edges.  An example of a key node in a class hierarchy is the node representing owl:Thing.  In a way
     * they are just important or distinguished nodes.
     * @param callback A callback which will handle the result.  Not {@code null}.
     * @throws NullPointerException if {@code callback} is {@code null}.
     */
    void getKeyNodes(GetKeyNodesCallback<U> callback);

    /**
     * Gets the successor nodes of the specified predecessorNode.
     * @param userObject The user object.  Not {@code null}.
     * @param callback A callback to handle the result. Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    void getSuccessorNodes(U userObject, GetSuccessorNodesCallback<U> callback);

    void getPathsFromKeyNodes(U toUserObject, GetPathsBetweenNodesCallback<U> paths);

    void getPathsBetweenNodes(U fromUserObject, U toUserObject, GetPathsBetweenNodesCallback<U> paths);

    /**
     * Adds a listener to this graph model.
     * @param handler The listener to be added.  Not {@code null}.
     * @return The registration for the listener. Not {@code null}.
     * @throws NullPointerException if {@code listener} is {@code null}.
     */
    HandlerRegistration addGraphModelHandler(GraphModelChangedHandler<U> handler);

}
