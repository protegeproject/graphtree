package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/01/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class GraphModelChangeTidier_TestCase<T extends Serializable> {

    @Mock
    private GraphEdge<T> edge;

    @Test
    public void addEdgeShouldReturnAddEdge() {
        List<GraphModelChange<T>> changes = Lists.newArrayList();
        changes.add(new AddEdge<T>(edge));
        GraphModelChangeTidier<T> filter = new GraphModelChangeTidier<T>(changes);
        List<GraphModelChange<T>> result = filter.getTidiedChanges();
        assertEquals(changes, result);
    }

    @Test
    public void removeEdgeShouldReturnRemoveEdge() {
        List<GraphModelChange<T>> changes = Lists.newArrayList();
        changes.add(new RemoveEdge<T>(edge));
        GraphModelChangeTidier<T> filter = new GraphModelChangeTidier<T>(changes);
        List<GraphModelChange<T>> result = filter.getTidiedChanges();
        assertEquals(changes, result);
    }

    @Test
    public void addFollowedByRemoveShouldMinimiseToEmptyList() {
        List<GraphModelChange<T>> changes = Lists.newArrayList();
        changes.add(new AddEdge<T>(edge));
        changes.add(new RemoveEdge<T>(edge));
        GraphModelChangeTidier<T> filter = new GraphModelChangeTidier<T>(changes);
        List<GraphModelChange<T>> result = filter.getTidiedChanges();
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeFollowedByAddShouldBeReordered() {
        List<GraphModelChange<T>> changes = Lists.newArrayList();
        RemoveEdge<T> removeEdge = new RemoveEdge<T>(mock(GraphEdge.class));
        changes.add(removeEdge);
        AddEdge<T> addEdge = new AddEdge<T>(mock(GraphEdge.class));
        changes.add(addEdge);
        GraphModelChangeTidier<T> filter = new GraphModelChangeTidier<T>(changes);
        List<GraphModelChange<T>> result = filter.getTidiedChanges();
        List<GraphModelChange<T>> expected = Lists.newArrayList();
        expected.add(addEdge);
        expected.add(removeEdge);
        assertThat(result, equalTo(expected));
    }
}
