package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.tree.NodeUserObjectChanged;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class NodeUserObjectChangedHandler_TestCase<U extends Serializable> {

    public static final String NEW_RENDERING = "Rendering";

    @Mock
    private TreeNodeId node;

    @Mock
    private TreeNodeView<U> view;

    @Mock
    private TreeNodeViewMapper<U> viewMapper;

    @Mock
    private NodeUserObjectChanged<U> nodeUserObjectChanged;

    @Mock
    private U userObject;

    private NodeUserObjectChangedHandler<U> handler;

    @Mock
    private TreeNodeRenderer<U> renderer;

    @Before
    public void setUp() {
        when(nodeUserObjectChanged.getTreeNodeId()).thenReturn(node);
        when(nodeUserObjectChanged.getUserObject()).thenReturn(userObject);
        when(viewMapper.getViewIfPresent(node)).thenReturn(Optional.of(view));
        when(renderer.getHtmlRendering(userObject)).thenReturn(NEW_RENDERING);
        handler = new NodeUserObjectChangedHandler<>(viewMapper, renderer);
    }

    @Test
    public void setNodeRenderingShouldBeCalledWithNewRenderingAsArgument() {
        handler.handleNodeUserObjectChanged(nodeUserObjectChanged);
        verify(view, times(1)).setRendering(NEW_RENDERING);
    }


}
