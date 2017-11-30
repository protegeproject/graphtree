package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.UserObjectKeyProvider;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/07/2013
 * <p>
 *     The basic interface to a graph structure - a set of nodes and successor relationships.  The key of a user object
 *     of a graph node uniquely determines it.  Keys are provides by an instance of {@link UserObjectKeyProvider}.
 * </p>
 */
public interface GraphModel<U extends Serializable, K> {

    /**
     * Gets the root nodes of the graph.  These are similar to source nodes in a directed graph except that they may
     * have incoming edges.  An example of a root node in a class hierarchy is the node representing owl:Thing.  In a way
     * they are just important or distinguished nodes.
     * @param callback A callback which will handle the result. 
     * @throws NullPointerException if {@code callback} is {@code null}.
     */
    void getRootNodes(GetRootNodesCallback<U> callback);

    /**
     * Gets the successor nodes of the specified predecessorNode.
     * @param predecessorUserObjectKey A user object key that identifies the node.
     * @param callback A callback to handle the result.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    void getSuccessorNodes(@Nonnull K predecessorUserObjectKey,
                           @Nonnull GetSuccessorNodesCallback<U> callback);

    void getPathsFromRootNodes(@Nonnull K toUserObjectKey,
                               @Nonnull GetPathsBetweenNodesCallback<U> paths);

    void getPathsBetweenNodes(U fromUserObject, U toUserObject, GetPathsBetweenNodesCallback<U> paths);

    /**
     * Adds a listener to this graph model.
     * @param handler The listener to be added. 
     * @return The registration for the listener.
     * @throws NullPointerException if {@code listener} is {@code null}.
     */
    HandlerRegistration addGraphModelHandler(GraphModelChangedHandler<U> handler);

}
