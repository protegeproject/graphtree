package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.base.Objects;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/07/2013
 * <p>
 *     A {@link GraphNode} represents a node in a graph.  The user object of the graph node uniquely determines it - i.e.
 *     there is a one to one mapping asPath user objects to {@link GraphNode}s.  Each node can provide a rendering of
 *     its user object.
 * </p>
 */
public class GraphNode<U extends Serializable> implements Serializable, IsSerializable {

    private U userObject;

    private boolean sink;

    private GraphNode() {
    }

    public GraphNode(U userObject) {
        this(userObject, false);
    }

    public GraphNode(U userObject, boolean sink) {
        this(userObject, userObject.toString(), sink);
    }

    public static <U extends Serializable> GraphNode<U> get(U userObject) {
        return new GraphNode<U>(userObject);
    }

    public static <U extends Serializable> GraphNode<U> get(U userObject, boolean sink) {
        return new GraphNode<U>(userObject, sink);
    }

    public GraphNode(U userObject, String shortForm, boolean sink) {
        this.userObject = checkNotNull(userObject);
        this.sink = sink;
    }

    public U getUserObject() {
        return userObject;
    }

    public boolean isSink() {
        return sink;
    }

//    @SuppressWarnings("unchecked")
//    public int compareTo(GraphNode graphNode) {
//        return shortForm.compareToIgnoreCase(graphNode.getShortForm());
//    }

    @Override
    public int hashCode() {
        return "DAGNode".hashCode() + userObject.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GraphNode)) {
            return false;
        }
        GraphNode other = (GraphNode) obj;
        return this.userObject.equals(other.userObject);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("GraphNode").addValue(userObject).toString();
    }
}

