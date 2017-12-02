package edu.stanford.protege.gwt.graphtree.shared.graph;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/02/2014
 */
public class GraphNodeMatcher<U extends Serializable> extends TypeSafeMatcher<GraphNode<U>> {

    private final U userObject;

    public GraphNodeMatcher(U userObject) {
        this.userObject = userObject;
    }

    @Override
    protected boolean matchesSafely(GraphNode<U> item) {
        return item.getUserObject().equals(userObject);
    }

    @Override
    public void describeTo(Description description) {
    }

    public static <U extends Serializable> GraphNodeMatcher<U> graphNodeWithUserObject(U userObject) {
        return new GraphNodeMatcher<U>(userObject);
    }
}
