package edu.stanford.protege.gwt.graphtree.shared.graph;

import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.graph.impl.local.SimpleGraphModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Collection;

import static java.util.Collections.singletonList;
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
        SimpleGraphModel<U, String> model = SimpleGraphModel.<U, String>builder(Object::toString)
                .addRootNode(A)
                .addEdge(A, B)
                .addEdge(B, C)
                .build();
        model.getPathsFromRootNodes(C.toString(), callback);
        verify(callback, times(1)).handlePaths(captor.capture());
        Path<GraphNode<U>> expected = Path.asPath(
                GraphNode.get(A),
                GraphNode.get(B),
                GraphNode.get(C));
        assertEquals(singletonList(expected), captor.getValue());
    }

}
