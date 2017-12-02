package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/01/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class PathFinder_TestCase<U extends Serializable> {

    @Mock
    U A, B, C, D;
    

    @Test
    public void shouldReturnEmptyCollectionForFromNotInGraph() {
        final Multimap<U, U> successorMap = HashMultimap.create();
        HasSuccessors<U> hasSuccessors = new HasSuccessorsImpl<>(successorMap);
        PathFinder<U> pathFinder = new PathFinder<>(hasSuccessors);
        Collection<Path<U>> result = pathFinder.getPaths(A, C);
        assertEquals(Collections.<Path<U>>emptySet(), result);
    }

    @Test
    public void shouldReturnEmptyCollectionForToNotInGraph() {
        final Multimap<U, U> successorMap = HashMultimap.create();
        HasSuccessors<U> hasSuccessors = new HasSuccessorsImpl<>(successorMap);
        successorMap.put(A, B);
        PathFinder<U> pathFinder = new PathFinder<>(hasSuccessors);
        Collection<Path<U>> result = pathFinder.getPaths(A, C);
        assertEquals(Collections.<Path<U>>emptySet(), result);
    }


    @Test
    public void shouldReturnSingleElementListForDirectLoop() {
        final Multimap<U, U> successorMap = HashMultimap.create();
        successorMap.put(A, A);
        HasSuccessors<U> hasSuccessors = new HasSuccessorsImpl<>(successorMap);
        PathFinder<U> pathFinder = new PathFinder<>(hasSuccessors);
        Collection<Path<U>> result = pathFinder.getPaths(A, A);
        Path<U> expectedPath = Path.asPath(A);
        assertEquals(Collections.singleton(expectedPath), result);
    }

    @Test
    public void shouldReturnSinglePathForLoop() {
        final Multimap<U, U> successorMap = HashMultimap.create();
        successorMap.put(A, B);
        successorMap.put(B, C);
        successorMap.put(C, A);
        HasSuccessors<U> hasSuccessors = new HasSuccessorsImpl<>(successorMap);
        PathFinder<U> pathFinder = new PathFinder<>(hasSuccessors);
        Collection<Path<U>> result = pathFinder.getPaths(A, C);
        Path<U> expectedPath = Path.asPath(A, B, C);
        assertEquals(Collections.singleton(expectedPath), result);
    }

    @Test
    public void shouldTerminateOnLoop() {
        final Multimap<U, U> successorMap = HashMultimap.create();
        successorMap.put(A, B);
        successorMap.put(B, A);
        HasSuccessors<U> hasSuccessors = new HasSuccessorsImpl<>(successorMap);
        PathFinder<U> pathFinder = new PathFinder<>(hasSuccessors);
        Collection<Path<U>> result = pathFinder.getPaths(A, C);
        assertEquals(Collections.<Path<U>>emptySet(), result);
    }

    @Test
    public void shouldReturnSuccessorForDirectPath() {
        final Multimap<U, U> successorMap = HashMultimap.create();
        successorMap.put(A, B);
        HasSuccessors<U> hasSuccessors = new HasSuccessorsImpl<>(successorMap);
        PathFinder<U> pathFinder = new PathFinder<>(hasSuccessors);
        Collection<Path<U>> result = pathFinder.getPaths(A, B);
        Path<U> expectedPath = Path.asPath(A, B);
        assertEquals(Collections.singleton(expectedPath), result);
    }

    @Test
    public void shouldReturnSuccessorsSuccessorForPathOfLength2() {
        final Multimap<U, U> successorMap = HashMultimap.create();
        successorMap.put(A, B);
        successorMap.put(B, C);
        HasSuccessors<U> hasSuccessors = new HasSuccessorsImpl<>(successorMap);
        PathFinder<U> pathFinder = new PathFinder<>(hasSuccessors);
        Collection<Path<U>> result = pathFinder.getPaths(A, C);
        Path<U> expectedPath = Path.asPath(A, B, C);
        assertEquals(Collections.singleton(expectedPath), result);
    }

    @Test
    public void shouldFindPathReachedViaLoop() {
        final Multimap<U, U> successorMap = HashMultimap.create();
        successorMap.put(A, B);
        successorMap.put(B, B);
        successorMap.put(B, C);
        HasSuccessors<U> hasSuccessors = new HasSuccessorsImpl<>(successorMap);
        PathFinder<U> pathFinder = new PathFinder<>(hasSuccessors);
        Collection<Path<U>> result = pathFinder.getPaths(A, C);
        Path<U> expectedPath = Path.asPath(A, B, C);
        assertEquals(Collections.singleton(expectedPath), result);
    }

    @Test
    public void shouldReturnTwoPathsForDiamondShape() {
        final Multimap<U, U> successorMap = HashMultimap.create();
        successorMap.put(A, B);
        successorMap.put(B, D);
        successorMap.put(A, C);
        successorMap.put(C, D);
        HasSuccessors<U> hasSuccessors = new HasSuccessorsImpl<>(successorMap);
        PathFinder<U> pathFinder = new PathFinder<>(hasSuccessors);
        Collection<Path<U>> result = pathFinder.getPaths(A, D);
        Set<Path<U>> expectedPaths = new HashSet<>();
        expectedPaths.add(Path.asPath(A, B, D));
        expectedPaths.add(Path.asPath(A, C, D));
        assertEquals(expectedPaths, result);
    }



    private static class HasSuccessorsImpl<N> implements HasSuccessors<N> {

        private final Multimap<N, N> map;

        private HasSuccessorsImpl(Multimap<N, N> map) {
            this.map = map;
        }

        @Override
        public Iterable<N> getSuccessors(N node) {
            return map.get(node);
        }
    }
}
