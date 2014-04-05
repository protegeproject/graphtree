package edu.stanford.protege.gwt.graphtree.shared.graph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/01/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class GraphNode_TestCase<T extends Serializable> {

    @Mock
    private T userObject;

    @Mock
    private T otherUserObject;

    @Test
    public void shouldReturnEqualForNodesWithEqualUserObjects() {
        GraphNode<T> nodeA = new GraphNode<T>(userObject, false);
        GraphNode<T> nodeB = new GraphNode<T>(userObject, false);
        assertEquals(nodeA, nodeB);
    }

    @Test
    public void shouldReturnEqualHashCodesForNodesWithEqualUserObjects() {
        GraphNode<T> nodeA = new GraphNode<T>(userObject, false);
        GraphNode<T> nodeB = new GraphNode<T>(userObject, false);
        assertEquals(nodeA.hashCode(), nodeB.hashCode());
    }

    @Test
    public void shouldReturnEqualForNodesWithEqualUserObjectsButDifferentSinkFlags() {
        GraphNode<T> nodeA = new GraphNode<T>(userObject, false);
        GraphNode<T> nodeB = new GraphNode<T>(userObject, true);
        assertEquals(nodeA, nodeB);
    }

    @Test
    public void shouldReturnNotEqualForNodesWithDifferentUserObjects() {
        GraphNode<T> nodeA = new GraphNode<T>(userObject, false);
        GraphNode<T> nodeB = new GraphNode<T>(otherUserObject, false);
        assertFalse(nodeA.equals(nodeB));
    }

    @Test
    public void shouldReturnFalseForEqualsCalledWithNullArgument() {
        GraphNode<T> node = new GraphNode<T>(userObject);
        assertFalse(node.equals(null));
    }

    @Test
    public void isSinkShouldReturnSuppliedSinkFlag() {
        GraphNode<T> node = new GraphNode<T>(userObject, true);
        assertTrue(node.isSink());
    }


}
