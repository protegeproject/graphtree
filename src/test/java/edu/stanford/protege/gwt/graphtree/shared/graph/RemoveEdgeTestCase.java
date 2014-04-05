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
public class RemoveEdgeTestCase<T extends Serializable> {

    @Mock
    private GraphEdge<T> edge;

    @Test
    public void equalsShouldReturnTrueForEqualEdges() {
        RemoveEdge<T> removeEdgeA = new RemoveEdge<T>(edge);
        RemoveEdge<T> removeEdgeB = new RemoveEdge<T>(edge);
        assertThat(removeEdgeA, equalTo(removeEdgeB));
    }

    @Test
    public void hashCodeShouldReturnTrueForEqualEdges() {
        RemoveEdge<T> removeEdgeA = new RemoveEdge<T>(edge);
        RemoveEdge<T> removeEdgeB = new RemoveEdge<T>(edge);
        assertThat(removeEdgeA.hashCode(), equalTo(removeEdgeB.hashCode()));
    }

    @Test
    public void equalToNullShouldReturnFalse() {
        RemoveEdge<T> removeEdge = new RemoveEdge<T>(edge);
        assertThat(removeEdge.equals(null), equalTo(false));
    }

    @Test
    public void getReverseChangeShouldReturnRemoveEdgeWithSameEdge() {
        RemoveEdge<T> removeEdge = new RemoveEdge<T>(edge);
        AddEdge<T> expected = new AddEdge<T>(edge);
        assertThat(removeEdge.getReverseChange(), equalTo(expected));
    }
}
