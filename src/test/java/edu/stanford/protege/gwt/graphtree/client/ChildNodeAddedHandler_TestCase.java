package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import edu.stanford.protege.gwt.graphtree.shared.tree.ChildNodeAdded;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
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
public class ChildNodeAddedHandler_TestCase<U extends Serializable> {

    @Mock
    private TreeNodeViewManager<U> viewManager;

    @Mock
    private TreeNodeId parentNodeId;

    @Mock
    private TreeNodeData<U> childNodeData;

    @Mock
    private TreeNodeView<U> parentView;

    @Mock
    private TreeNodeView<U> childView;

    @Mock
    private ChildNodeAdded<U> childNodeAdded;

    private ChildNodeAddedHandler<U> handler;

    @Before
    public void setup() {
        when(viewManager.getViewIfPresent(parentNodeId)).thenReturn(Optional.of(parentView));
        when(viewManager.getView(childNodeData)).thenReturn(childView);
        when(childNodeAdded.getChildNode()).thenReturn(childNodeData);
        when(childNodeAdded.getParentNode()).thenReturn(parentNodeId);
        handler = new ChildNodeAddedHandler<U>(viewManager);
    }

    @Test
    public void addChildViewShouldBeCalledOnParent() {
        handler.handleChildNodeAdded(childNodeAdded);
        verify(parentView, times(1)).addChildView(childView);
    }

    @Test
    public void setDepthShouldBeCalledOnTheChildWithADepthOfParentDepthPlusOne() {
        int parentDepth = 3;
        when(parentView.getDepth()).thenReturn(parentDepth);
        handler.handleChildNodeAdded(childNodeAdded);
        verify(childView, times(1)).setDepth(parentDepth + 1);
    }

    @Test
    public void setLeafFalseShouldBeCalledIfParentIsEmpty() {
        when(parentView.isEmpty()).thenReturn(true);
        handler.handleChildNodeAdded(childNodeAdded);
        verify(parentView, times(1)).setLeaf(false);
    }

    @Test
    public void setLeafFalseShouldBeCalledIfParentIsNotEmpty() {
        when(parentView.isEmpty()).thenReturn(false);
        handler.handleChildNodeAdded(childNodeAdded);
        verify(parentView, times(1)).setLeaf(false);
    }

    @Test
    public void getViewShouldBeCalledOnViewManager() {
        handler.handleChildNodeAdded(childNodeAdded);
        verify(viewManager, times(1)).getView(childNodeData);
    }

}
