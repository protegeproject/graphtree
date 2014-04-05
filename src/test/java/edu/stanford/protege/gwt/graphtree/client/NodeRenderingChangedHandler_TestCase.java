package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import edu.stanford.protege.gwt.graphtree.shared.tree.NodeRenderingChanged;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class NodeRenderingChangedHandler_TestCase<U extends Serializable> {

    public static final String NEW_RENDERING = "Rendering";

    @Mock
    private TreeNodeId node;

    @Mock
    private TreeNodeView<U> view;

    @Mock
    private TreeNodeViewMapper<U> viewMapper;

    @Mock
    private NodeRenderingChanged<U> nodeRenderingChanged;

    private NodeRenderingChangedHandler<U> handler;

    @Before
    public void setUp() {
        when(nodeRenderingChanged.getHtmlRendering()).thenReturn(NEW_RENDERING);
        when(nodeRenderingChanged.getTreeNode()).thenReturn(node);
        when(viewMapper.getViewIfPresent(node)).thenReturn(Optional.of(view));
        handler = new NodeRenderingChangedHandler<U>(viewMapper);
    }

    @Test
    public void setNodeRenderingShouldBeCalledWithNewRenderingAsArgument() {
        handler.handleNodeRenderingChanged(nodeRenderingChanged);
        verify(view, times(1)).setRendering(NEW_RENDERING);
    }


}
