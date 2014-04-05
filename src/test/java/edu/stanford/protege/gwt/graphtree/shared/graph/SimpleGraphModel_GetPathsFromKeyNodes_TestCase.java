package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.Lists;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.graph.impl.local.SimpleGraphModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleGraphModel_GetPathsFromKeyNodes_TestCase<U extends Serializable> {

    @Mock
    private GetPathsBetweenNodesCallback<U> callback;

    @Captor
    private ArgumentCaptor<Collection<Path<GraphNode<U>>>> captor;

    @Mock
    U A, B, C;
    
    @Test
    public void shouldReturnPath() {
        SimpleGraphModel<U> model = SimpleGraphModel.<U>builder()
                .addKeyNode(A)
                .addEdge(A, B)
                .addEdge(B, C)
                .build();
        model.getPathsFromKeyNodes(C, callback);
        verify(callback, times(1)).handlePaths(captor.capture());
        Path<GraphNode<U>> expected = Path.asPath(
                GraphNode.<U>get(A),
                GraphNode.<U>get(B),
                GraphNode.<U>get(C));
        assertEquals(Arrays.asList(expected), captor.getValue());
    }

}
