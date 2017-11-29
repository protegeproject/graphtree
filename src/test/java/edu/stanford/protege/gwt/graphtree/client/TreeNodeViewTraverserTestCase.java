package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeNodeViewTraverserTestCase<U extends Serializable> {

    @Mock
    private TreeNodeView<U> view, parent, child, childSibling, parentSibling;

    private final TreeNodeViewTraverser<U> traverser = new TreeNodeViewTraverser<U>();

    @Test
    public void getNextShouldReturnSiblingOfCollapsedNode() {
        when(view.isExpanded()).thenReturn(false);
        when(view.getNextSibling()).thenReturn(java.util.Optional.of(childSibling));
        when(view.getParentView()).thenReturn(getAbsent());
        java.util.Optional<TreeNodeView<U>> next = traverser.getNext(view);
        assertTrue(next.isPresent());
        assertEquals(Optional.of(childSibling), next);
    }

    private java.util.Optional<TreeNodeView<U>> getAbsent() {
        return java.util.Optional.<TreeNodeView<U>>empty();
    }

    @Test
    public void getNextShouldReturnChildOfExpandedNode() {
        when(view.isExpanded()).thenReturn(true);
        when(view.getParentView()).thenReturn(getAbsent());
        when(view.getFirstChildView()).thenReturn(java.util.Optional.of(child));
        java.util.Optional<TreeNodeView<U>> next = traverser.getNext(view);
        assertTrue(next.isPresent());
        assertEquals(Optional.of(child), next);
    }

    @Test
    public void getNextShouldReturnSiblingOfParentNodeOfLastChild() {
        when(parent.isExpanded()).thenReturn(true);
        when(parent.getFirstChildView()).thenReturn(java.util.Optional.of(child));
        when(parent.getParentView()).thenReturn(getAbsent());
        when(parent.getNextSibling()).thenReturn(java.util.Optional.of(parentSibling));
        when(child.getParentView()).thenReturn(java.util.Optional.of(parent));
        when(child.getNextSibling()).thenReturn(getAbsent());
        java.util.Optional<TreeNodeView<U>> next = traverser.getNext(child);
        assertTrue(next.isPresent());
        assertEquals(Optional.of(parentSibling), next);
    }


    @Test
    public void getPathToRootShouldReturnPathOfSizeOneForNoParent() {
        when(view.getParentView()).thenReturn(getAbsent());
        Path<TreeNodeView<U>> path = traverser.getPathToRoot(view);
        assertEquals(1, path.size());
        assertEquals(Optional.of(view), path.getFirst());
    }

    @Test
    public void getPathToRootShouldReturnPathWithParent() {
        when(parent.getParentView()).thenReturn(getAbsent());
        when(view.getParentView()).thenReturn(java.util.Optional.of(parent));
        Path<TreeNodeView<U>> path = traverser.getPathToRoot(view);
        assertEquals(2, path.size());
        List<TreeNodeView<U>> expected = new ArrayList<TreeNodeView<U>>();
        expected.add(parent);
        expected.add(view);
        assertThat(path.asList(), equalTo(expected));
    }


    @Test
    public void getFirstVisibleAncestorReturnsParentWhenParentIsVisibleAndCollapsed() {
        when(parent.isExpanded()).thenReturn(false);
        when(parent.getParentView()).thenReturn(getAbsent());
        when(view.getParentView()).thenReturn(java.util.Optional.of(parent));
        java.util.Optional<TreeNodeView<U>> ancestor = traverser.getFirstVisibleAncestor(view);
        assertEquals(Optional.of(parent), ancestor);
    }

    @Test
    public void getFirstVisibleAncestorReturnsParentWhenParentIsVisibleAndExpanded() {
        when(parent.isExpanded()).thenReturn(true);
        when(parent.getParentView()).thenReturn(getAbsent());
        when(view.getParentView()).thenReturn(java.util.Optional.of(parent));
        java.util.Optional<TreeNodeView<U>> ancestor = traverser.getFirstVisibleAncestor(view);
        assertEquals(Optional.of(parent), ancestor);
    }
}
