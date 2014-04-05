package edu.stanford.protege.gwt.graphtree.shared.graph;


import static edu.stanford.protege.gwt.graphtree.shared.graph.GraphNodeMatcher.graphNodeWithUserObject;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import com.google.common.collect.Sets;
import edu.stanford.protege.gwt.graphtree.shared.graph.impl.local.SimpleGraphModel;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Collection;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleGraphModel_GetSuccessorNodes_TestCase<U extends Serializable> {

    @Mock
    private GetSuccessorNodesCallback<U> callback;

    @Captor
    private ArgumentCaptor<SuccessorMap<U>> successorsCaptor;

    @Mock
    private U A, B, C;

    @Test
    public void getNodes_ShouldReturn_SpecifiedSuccessors() {
        SimpleGraphModel<U> model = SimpleGraphModel.<U>builder()
                .addEdge(A, B)
                .addEdge(A, C)
                .build();
        model.getSuccessorNodes(A, callback);
        verify(callback, times(1)).handleSuccessorNodes(successorsCaptor.capture());
        SuccessorMap<U> result = successorsCaptor.getValue();
        assertThat(result.size(), equalTo(1));
        Collection<GraphNode<U>> successors = result.getSuccessors();
        assertThat(successors, IsCollectionContaining.<GraphNode<U>>hasItem(graphNodeWithUserObject(B)));
        assertThat(successors, IsCollectionContaining.<GraphNode<U>>hasItem(graphNodeWithUserObject(C)));
    }
}
