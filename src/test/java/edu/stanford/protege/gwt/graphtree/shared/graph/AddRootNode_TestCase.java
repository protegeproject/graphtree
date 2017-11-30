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
public class AddRootNode_TestCase<T extends Serializable> {

    @Mock
    private GraphNode<T> node;

    @Test
    public void equalsShouldReturnTrueForEqualNodes() {
        AddRootNode<T> addRootNodeA = new AddRootNode<T>(node);
        AddRootNode<T> addRootNodeB = new AddRootNode<T>(node);
        assertThat(addRootNodeA, equalTo(addRootNodeB));
    }

    @Test
    public void hashCodeShouldReturnTrueForEqualNodes() {
        AddRootNode<T> addRootNodeA = new AddRootNode<T>(node);
        AddRootNode<T> addRootNodeB = new AddRootNode<T>(node);
        assertThat(addRootNodeA.hashCode(), equalTo(addRootNodeB.hashCode()));
    }

    @Test
    public void equalToNullShouldReturnFalse() {
        AddRootNode<T> addRootNode = new AddRootNode<T>(node);
        assertThat(addRootNode.equals(null), equalTo(false));
    }

    @Test
    public void getReverseChangeShouldReturnRemoveEdgeWithSameEdge() {
        AddRootNode<T> addRootNodeA = new AddRootNode<T>(node);
        RemoveRootNode<T> expected = new RemoveRootNode<T>(node);
        assertThat(addRootNodeA.getReverseChange(), equalTo(expected));
    }
}
