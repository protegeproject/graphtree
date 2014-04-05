package edu.stanford.protege.gwt.graphtree.shared.graph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class AddEdge_TestCase<T extends Serializable> {

    @Mock
    private GraphEdge<T> edge;

    @Test
    public void equalsShouldReturnTrueForEqualEdges() {
        AddEdge<T> addEdgeA = new AddEdge<T>(edge);
        AddEdge<T> addEdgeB = new AddEdge<T>(edge);
        assertThat(addEdgeA, equalTo(addEdgeB));
    }

    @Test
    public void hashCodeShouldReturnTrueForEqualEdges() {
        AddEdge<T> addEdgeA = new AddEdge<T>(edge);
        AddEdge<T> addEdgeB = new AddEdge<T>(edge);
        assertThat(addEdgeA.hashCode(), equalTo(addEdgeB.hashCode()));
    }

    @Test
    public void equalToNullShouldReturnFalse() {
        AddEdge<T> addEdge = new AddEdge<T>(edge);
        assertThat(addEdge.equals(null), equalTo(false));
    }

    @Test
    public void getReverseChangeShouldReturnRemoveEdgeWithSameEdge() {
        AddEdge<T> addEdge = new AddEdge<T>(edge);
        RemoveEdge<T> expected = new RemoveEdge<T>(edge);
        assertThat(addEdge.getReverseChange(), equalTo(expected));
    }

}
