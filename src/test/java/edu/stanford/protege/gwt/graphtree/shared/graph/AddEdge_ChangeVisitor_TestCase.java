package edu.stanford.protege.gwt.graphtree.shared.graph;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.times;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class AddEdge_ChangeVisitor_TestCase<T extends Serializable> {

    @Mock
    private GraphEdge<T> edge;

    @Mock
    private GraphModelChangeVisitor<T> visitor;


    @Captor
    private ArgumentCaptor<AddEdge<T>> captor;


    @Test
    public void AddEdgeAcceptShouldVisitAddEdge() {
        AddEdge<T> addEdge = new AddEdge<T>(edge);
        addEdge.accept(visitor);
        Mockito.verify(visitor, times(1)).visit(captor.capture());
        assertThat(captor.getValue(), equalTo(addEdge));
    }
}
