package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.tree.ChildNodeRemoved;
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
public class ChildNodeRemovedHandler_TestCase<U extends Serializable> {

    @Mock
    private TreeNodeViewManager<U> viewManager;

    @Mock
    private TreeNodeId parentNode;

    @Mock
    private TreeNodeId childNode;

    @Mock
    private TreeNodeView<U> parentView;

    @Mock
    private TreeNodeView<U> childView;

    @Mock
    private ChildNodeRemoved<U> childNodeRemoved;

    private ChildNodeRemovedHandler<U> handler;

    @Before
    public void setup() {
        when(viewManager.getViewIfPresent(parentNode)).thenReturn(Optional.of(parentView));
        when(viewManager.getViewIfPresent(childNode)).thenReturn(Optional.of(childView));
        when(childNodeRemoved.getChildNode()).thenReturn(childNode);
        when(childNodeRemoved.getParentNode()).thenReturn(parentNode);
        handler = new ChildNodeRemovedHandler<U>(viewManager);
    }


    @Test
    public void removeChildViewShouldBeCalledOnParent() {
        handler.handleChildNodeRemoved(childNodeRemoved);
        verify(parentView, times(1)).removeChildView(childView);
    }

    @Test
    public void setLeafTrueShouldBeCalledIfParentIsEmpty() {
        when(parentView.isEmpty()).thenReturn(true);
        handler.handleChildNodeRemoved(childNodeRemoved);
        verify(parentView, times(1)).setLeaf(true);
    }

    @Test
    public void setLeafFalseShouldBeCalledIfParentIsNotEmpty() {
        when(parentView.isEmpty()).thenReturn(false);
        handler.handleChildNodeRemoved(childNodeRemoved);
        verify(parentView, times(1)).setLeaf(false);
    }

    @Test
    public void releaseViewShouldBeCalledOnViewManager() {
        handler.handleChildNodeRemoved(childNodeRemoved);
        verify(viewManager, times(1)).releaseView(childNode);
    }


}
