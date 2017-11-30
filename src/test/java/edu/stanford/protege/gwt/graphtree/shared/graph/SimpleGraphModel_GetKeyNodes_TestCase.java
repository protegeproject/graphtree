package edu.stanford.protege.gwt.graphtree.shared.graph;

import edu.stanford.protege.gwt.graphtree.shared.graph.impl.local.SimpleGraphModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleGraphModel_GetKeyNodes_TestCase<U extends Serializable> {

    @Captor
    private ArgumentCaptor<List<GraphNode<U>>> keyNodesCaptor;

    @Mock
    private GetRootNodesCallback<U> callback;

    @Mock
    U A, B;
    
    @Test
    public void getKeyNodes_ShouldReturn_SpecifiedKeyNodes_InSpecifiedOrder() {
        SimpleGraphModel<U, String> model = SimpleGraphModel.<U, String>builder(Object::toString)
                .addRootNode(A)
                .addRootNode(B)
                .build();
        model.getRootNodes(callback);
        verify(callback, times(1)).handleRootNodes(keyNodesCaptor.capture());
        List<GraphNode<U>> value = keyNodesCaptor.getValue();
        assertEquals(2, value.size());
        GraphNode graphNodeA = value.get(0);
        assertEquals(A, graphNodeA.getUserObject());
        assertTrue(graphNodeA.isSink());
        GraphNode graphNodeB = value.get(1);
        assertEquals(B, graphNodeB.getUserObject());
        assertTrue(graphNodeB.isSink());
    }
}
