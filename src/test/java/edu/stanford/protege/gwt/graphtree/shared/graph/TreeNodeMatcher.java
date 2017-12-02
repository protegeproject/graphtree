package edu.stanford.protege.gwt.graphtree.shared.graph;

import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/02/2014
 */
public class TreeNodeMatcher<U extends Serializable> extends TypeSafeMatcher<TreeNodeData<U>> {

    private final Object userObject;

    public TreeNodeMatcher(Object userObject) {
        this.userObject = userObject;
    }

    @Override
    public boolean matchesSafely(TreeNodeData item) {
        return item.getUserObject().equals(userObject);
    }

    @Override
    public void describeTo(Description description) {
    }

    @Factory
    public static <U extends Serializable> Matcher<TreeNodeData<U>> thatIsATreeNodeWithUserObject(U userObject) {
        return new TreeNodeMatcher<>(userObject);
    }
}
