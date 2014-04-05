package edu.stanford.protege.gwt.graphtree.shared.graph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class SuccessorMap_TestCase<T extends Serializable> {

    @Mock
    private GraphNode<T> A;


    @Mock
    private GraphNode<T> B;


    @Mock
    private GraphNode<T> C;


    @Test
    public void builderBuildsExpectedMap() {
        SuccessorMap.Builder<T> builder = SuccessorMap.<T>builder();
        builder.add(A, B);
        builder.add(B, C);
        SuccessorMap<T> map = builder.build();
//        assertThat(map.getSuccessors(A), hasItem(B));
//        assertThat(map.getSuccessors(B), hasItem(C));
    }


    @Test
    public void getSizeReturnsTheNumberOfPredecessorNodes() {
        SuccessorMap.Builder<T> builder = SuccessorMap.<T>builder();
        builder.add(A, B);
        builder.add(B, C);
        SuccessorMap<T> map = builder.build();
        assertThat(map.size(), equalTo(2));
    }

    @Test
    public void isEmptyReturnsTrueWhenEmpty() {
        SuccessorMap.Builder<T> builder = SuccessorMap.<T>builder();
        SuccessorMap<T> map = builder.build();
        assertThat(map.isEmpty(), equalTo(true));
    }
}
