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
public class AddKeyNode_TestCase<T extends Serializable> {

    @Mock
    private GraphNode<T> node;

    @Test
    public void equalsShouldReturnTrueForEqualNodes() {
        AddKeyNode<T> addKeyNodeA = new AddKeyNode<T>(node);
        AddKeyNode<T> addKeyNodeB = new AddKeyNode<T>(node);
        assertThat(addKeyNodeA, equalTo(addKeyNodeB));
    }

    @Test
    public void hashCodeShouldReturnTrueForEqualNodes() {
        AddKeyNode<T> addKeyNodeA = new AddKeyNode<T>(node);
        AddKeyNode<T> addKeyNodeB = new AddKeyNode<T>(node);
        assertThat(addKeyNodeA.hashCode(), equalTo(addKeyNodeB.hashCode()));
    }

    @Test
    public void equalToNullShouldReturnFalse() {
        AddKeyNode<T> addKeyNode = new AddKeyNode<T>(node);
        assertThat(addKeyNode.equals(null), equalTo(false));
    }

    @Test
    public void getReverseChangeShouldReturnRemoveEdgeWithSameEdge() {
        AddKeyNode<T> addKeyNodeA = new AddKeyNode<T>(node);
        RemoveKeyNode<T> expected = new RemoveKeyNode<T>(node);
        assertThat(addKeyNodeA.getReverseChange(), equalTo(expected));
    }
}
