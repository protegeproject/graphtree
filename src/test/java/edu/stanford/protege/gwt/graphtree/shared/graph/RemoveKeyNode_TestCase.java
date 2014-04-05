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
public class RemoveKeyNode_TestCase<T extends Serializable> {

    @Mock
    private GraphNode<T> node;

    @Test
    public void equalsShouldReturnTrueForEqualNodes() {
        RemoveKeyNode<T> removeKeyNodeA = new RemoveKeyNode<T>(node);
        RemoveKeyNode<T> removeKeyNodeB = new RemoveKeyNode<T>(node);
        assertThat(removeKeyNodeA, equalTo(removeKeyNodeB));
    }

    @Test
    public void hashCodeShouldReturnTrueForEqualNodes() {
        RemoveKeyNode<T> removeKeyNodeA = new RemoveKeyNode<T>(node);
        RemoveKeyNode<T> removeKeyNodeB = new RemoveKeyNode<T>(node);
        assertThat(removeKeyNodeA.hashCode(), equalTo(removeKeyNodeB.hashCode()));
    }

    @Test
    public void equalToNullShouldReturnFalse() {
        RemoveKeyNode<T> removeKeyNode = new RemoveKeyNode<T>(node);
        assertThat(removeKeyNode.equals(null), equalTo(false));
    }

    @Test
    public void getReverseChangeShouldReturnRemoveEdgeWithSameEdge() {
        RemoveKeyNode<T> removeKeyNodeA = new RemoveKeyNode<T>(node);
        AddKeyNode<T> expected = new AddKeyNode<T>(node);
        assertThat(removeKeyNodeA.getReverseChange(), equalTo(expected));
    }

}
