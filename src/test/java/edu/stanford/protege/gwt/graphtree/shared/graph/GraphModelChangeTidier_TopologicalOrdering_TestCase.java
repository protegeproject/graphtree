package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class GraphModelChangeTidier_TopologicalOrdering_TestCase<T extends Serializable> {

    @Mock
    private GraphNode<T> nodeA;

    @Mock
    private GraphNode<T> nodeB;

    @Mock
    private GraphNode<T> nodeC;

    private GraphEdge<T> edgeAB;

    private GraphEdge<T> edgeBC;

    @Before
    public void initialiseEdges() {
        edgeAB = new GraphEdge<T>(nodeA, nodeB);
        edgeBC = new GraphEdge<T>(nodeB, nodeC);
    }

    @Test
    public void addEdge_AB_FollowedBy_addEdge_BC_shouldPreserveOrdering() {
        AddEdge<T> addAB = new AddEdge<T>(edgeAB);
        AddEdge<T> addBC = new AddEdge<T>(edgeBC);
        List<GraphModelChange<T>> changes = Lists.newArrayList();
        changes.add(addAB);
        changes.add(addBC);
        GraphModelChangeTidier<T> filter = new GraphModelChangeTidier<T>(changes);
        List<GraphModelChange<T>> result = filter.getTidiedChanges();
        List<GraphModelChange<T>> expected = Lists.newArrayList();
        expected.add(addAB);
        expected.add(addBC);
        assertThat(result, equalTo(expected));
    }

    @Test
    public void addEdge_BC_FollowedBy_addEdge_AB_shouldOrderChangesTopologically() {
        AddEdge<T> addAB = new AddEdge<T>(edgeAB);
        AddEdge<T> addBC = new AddEdge<T>(edgeBC);
        List<GraphModelChange<T>> changes = Lists.newArrayList();
        changes.add(addBC);
        changes.add(addAB);
        GraphModelChangeTidier<T> filter = new GraphModelChangeTidier<T>(changes);
        List<GraphModelChange<T>> result = filter.getTidiedChanges();
        List<GraphModelChange<T>> expected = Lists.newArrayList();
        expected.add(addAB);
        expected.add(addBC);
        assertThat(result, equalTo(expected));
    }

    @Test
    public void RemoveEdge_AB_FollowedBy_RemoveEdge_BC_ShouldOrderReverseTopologically() {
        RemoveEdge<T> remAB = new RemoveEdge<T>(edgeAB);
        RemoveEdge<T> remBC = new RemoveEdge<T>(edgeBC);
        List<GraphModelChange<T>> changes = Lists.newArrayList();
        changes.add(remAB);
        changes.add(remBC);
        GraphModelChangeTidier<T> filter = new GraphModelChangeTidier<T>(changes);
        List<GraphModelChange<T>> result = filter.getTidiedChanges();
        List<GraphModelChange<T>> expected = Lists.newArrayList();
        expected.add(remBC);
        expected.add(remAB);
        assertThat(result, equalTo(expected));
    }
}
