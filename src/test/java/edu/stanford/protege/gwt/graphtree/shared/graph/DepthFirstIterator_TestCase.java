package edu.stanford.protege.gwt.graphtree.shared.graph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/01/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class DepthFirstIterator_TestCase<U extends Serializable> {

    @Mock
    private HasSuccessors<U> hasSuccessors;

    @Mock
    U A, B, C, D;

    @Test
    public void shouldReturnParentFollowedByChild() {
        when(hasSuccessors.getSuccessors(A)).thenReturn(Collections.<U>singleton(B));
        when(hasSuccessors.getSuccessors(B)).thenReturn(Collections.<U>emptySet());
        DepthFirstIterator<U> iterator = new DepthFirstIterator<U>(A, hasSuccessors);
        assertTrue(iterator.hasNext());
        assertEquals(A, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(B, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void shouldReturnParentFollowedBySubsequentChildren() {
        when(hasSuccessors.getSuccessors(A)).thenReturn(Arrays.asList(B, C));
        when(hasSuccessors.getSuccessors(B)).thenReturn(Collections.<U>emptySet());
        when(hasSuccessors.getSuccessors(C)).thenReturn(Collections.<U>emptySet());
        DepthFirstIterator<U> iterator = new DepthFirstIterator<U>(A, hasSuccessors);
        assertTrue(iterator.hasNext());
        assertEquals(A, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(B, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(C, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void shouldReturnNodeWithMultipleIncomingEdgesOnlyOnce() {
        when(hasSuccessors.getSuccessors(A)).thenReturn(Arrays.asList(B, C));
        when(hasSuccessors.getSuccessors(B)).thenReturn(Collections.<U>singleton(D));
        when(hasSuccessors.getSuccessors(C)).thenReturn(Collections.<U>singleton(D));
        when(hasSuccessors.getSuccessors(D)).thenReturn(Collections.<U>emptySet());
        DepthFirstIterator<U> iterator = new DepthFirstIterator<U>(A, hasSuccessors);
        assertTrue(iterator.hasNext());
        assertEquals(A, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(B, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(D, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(C, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void shouldReturnNodesInCycleExactlyOnce() {
        when(hasSuccessors.getSuccessors(A)).thenReturn(Arrays.asList(B, C));
        when(hasSuccessors.getSuccessors(B)).thenReturn(Collections.<U>singleton(D));
        when(hasSuccessors.getSuccessors(C)).thenReturn(Collections.<U>emptySet());
        when(hasSuccessors.getSuccessors(D)).thenReturn(Collections.<U>singleton(A));
        DepthFirstIterator<U> iterator = new DepthFirstIterator<U>(A, hasSuccessors);
        assertTrue(iterator.hasNext());
        assertEquals(A, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(B, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(D, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(C, iterator.next());
        assertFalse(iterator.hasNext());
    }


}
