package edu.stanford.protege.gwt.graphtree.shared;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class PathTestCase<U extends Serializable> {

    @Mock
    U A, B, C, D;
    
    
    @Test
    public void getPathReturnsSuppliedList() {
        List<U> list = Arrays.asList(A, B, C);
        Path<U> path = new Path<>(list);
        assertThat(path.asList(), equalTo(Arrays.asList(A, B, C)));
    }

    @Test
    public void pathsWithSameElementsShouldBeEqual() {
        List<U> list = Arrays.asList(A, B, C);
        Path<U> pathA = new Path<>(list);
        Path<U> pathB = new Path<>(list);
        assertThat(pathA, equalTo(pathB));
    }

    @Test
    public void getLengthShouldReturnSuppliedLength() {
        Path<U> pathA = new Path<>(Arrays.asList(A, B, C));
        assertThat(pathA.getLength(), equalTo(3));
    }

    @Test
    public void getFirstShouldReturnOptionalAbsentForEmptyPath() {
        Path<U> path = new Path<>(Collections.<U>emptyList());
        assertEquals(Optional.<U>empty(), path.getFirst());
    }

    @Test
    public void getFirstShouldReturnFirstElement() {
        Path<?> path = new Path<>(Arrays.asList(A, B, C));
        assertEquals(Optional.of(A), path.getFirst());
    }

    @Test
    public void getLastShouldReturnOptionalAbsentForEmptyPath() {
        Path<?> path = new Path<>(Collections.emptyList());
        assertEquals(Optional.empty(), path.getLast());
    }

    @Test
    public void getLastShouldReturnLastElement() {
        Path<?> path = new Path<>(Arrays.asList(A, B, C));
        assertEquals(Optional.of(C), path.getLast());
    }

    @Test
    public void reverseReturnsReversedPath() {
        Path<U> path = new Path<>(Arrays.asList(A, B, C));
        Path<U> reverse = new Path<>(Arrays.asList(C, B, A));
        assertThat(path.reverse(), equalTo(reverse));
    }

    @Test
    public void getPathShouldReturnACopy() {
        List<U> list = Arrays.asList(A, B, C);
        Path<?> path = new Path<>(list);
        path.asList().remove(0);
        assertEquals(list, path.asList());
    }

    @Test
    public void getLastPredecessorReturnsCorrectValue() {
        List<U> list = Arrays.asList(A, B, C);
        Path<?> path = new Path<>(list);
        assertEquals(Optional.of(B), path.getLastPredecessor());
    }

    @Test
    public void asPathShouldReturnPathWithSuppliedElements() {
        Path<U> path = Path.asPath(A, B, C);
        assertEquals(Arrays.asList(A, B, C), path.asList());
    }

    @Test
    public void sizeShouldReturnSizeOfSuppliedPath() {
        Path<U> path = Path.asPath(A, B, C);
        assertEquals(3, path.size());
    }

    @Test
    public void getShouldReturnElementAtSpecifiedIndex() {
        Path<U> path = Path.asPath(A, B, C);
        assertEquals(A, path.get(0));
        assertEquals(B, path.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getWithNegativeIndexShouldReturnIndexOutOfBoundsException() {
        Path<U> path = Path.asPath(A, B, C);
        path.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getWithIndexEqualToSizeShouldThrowIndexOutOfBoundsException() {
        Path<U> path = Path.asPath(A, B, C);
        path.get(3);
    }

    @Test
    public void emptyPathShouldReturnEmptyPath() {
        Path<U> path = Path.emptyPath();
        assertNotNull(path);
        assertEquals(Collections.<U>emptyList(), path.asList());
    }

    @Test
    public void transformShouldApplyTransformToPathElements() {
        Path<U> path = Path.asPath(A, B, C);
        Path.Transform<U, U> t = element -> element;
        Path<U> expected = Path.asPath(A, B, C);
        assertThat(path.transform(t), equalTo(expected));
    }


}
