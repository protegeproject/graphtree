package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/01/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class TopologicalSorter_TestCase<T extends Serializable> {

    @Mock
    private GraphNode<T> A;

    @Mock
    private GraphNode<T> B;

    @Mock
    private GraphNode<T> C;

    @Test
    public void shouldReturnAbsentForNonDAGInput() {
        Multimap<GraphNode<T>, GraphNode<T>> graph = HashMultimap.create();
        graph.put(A, B);
        graph.put(B, C);
        graph.put(C, A);
        TopologicalSorter<T> sorter = new TopologicalSorter<>(graph);
        java.util.Optional<List<GraphNode<T>>> result = sorter.getTopologicalOrdering();
        assertTrue(!result.isPresent());
    }

    @Test
    public void shouldReturnAbsentForNonDAGInputForReverseTopologicalOrdering() {
        Multimap<GraphNode<T>, GraphNode<T>> graph = HashMultimap.create();
        graph.put(A, B);
        graph.put(B, C);
        graph.put(C, A);
        TopologicalSorter<T> sorter = new TopologicalSorter<>(graph);
        java.util.Optional<List<GraphNode<T>>> result = sorter.getTopologicalOrdering(TopologicalSorter.Direction.REVERSE);
        assertTrue(!result.isPresent());
    }

    @Test
    public void shouldReturnABC() {
        Multimap<GraphNode<T>, GraphNode<T>> graph = HashMultimap.create();
        graph.put(A, B);
        graph.put(B, C);
        TopologicalSorter<T> sorter = new TopologicalSorter<>(graph);
        java.util.Optional<List<GraphNode<T>>> result = sorter.getTopologicalOrdering();
        assertTrue(result.isPresent());
        List<GraphNode<T>> expected = Lists.newArrayList();
        expected.add(A);
        expected.add(B);
        expected.add(C);
        assertThat(result, equalTo(Optional.of(expected)));
    }

    @Test
    public void shouldReturnCBAForReverseTopologicalOrdering() {
        Multimap<GraphNode<T>, GraphNode<T>> graph = HashMultimap.create();
        graph.put(A, B);
        graph.put(B, C);
        TopologicalSorter<T> sorter = new TopologicalSorter<>(graph);
        java.util.Optional<List<GraphNode<T>>> result = sorter.getTopologicalOrdering(TopologicalSorter.Direction.REVERSE);
        assertTrue(result.isPresent());
        List<GraphNode<T>> expected = Lists.newArrayList();
        expected.add(C);
        expected.add(B);
        expected.add(A);
        assertThat(result, equalTo(Optional.of(expected)));
    }
}
