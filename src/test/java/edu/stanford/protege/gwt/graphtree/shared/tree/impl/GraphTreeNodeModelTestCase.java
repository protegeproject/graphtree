package edu.stanford.protege.gwt.graphtree.shared.tree.impl;

import com.google.common.collect.Sets;
import edu.stanford.protege.gwt.graphtree.shared.UserObjectKeyProvider;
import edu.stanford.protege.gwt.graphtree.shared.graph.impl.local.SimpleGraphModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.GetTreeNodesCallback;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class GraphTreeNodeModelTestCase<U extends Serializable> {

    @Captor
    ArgumentCaptor<List<TreeNodeData<U>>> captor;

    @Mock
    GetTreeNodesCallback<U> callback;

    @Mock
    U A, B, C, D, E;

    UserObjectKeyProvider<U> keyProvider = userObject -> userObject;

    @Test
    public void getNodes_Callback_isCalledForEmptyGraph() {
        SimpleGraphModel<U> graphModel = SimpleGraphModel.<U>builder().build();
        GraphTreeNodeModel<U> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getNodes(Optional.empty(), callback);
        verify(callback, times(1)).handleNodes(captor.capture());
    }

    @Test
    public void getNodes_with_OptionalAbsent_ShouldReturn_RootNodes() {
        SimpleGraphModel<U> graphModel = SimpleGraphModel.<U>builder().addKeyNode(A).build();
        GraphTreeNodeModel<U> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getNodes(Optional.empty(), callback);
        verify(callback, times(1)).handleNodes(captor.capture());
        List<TreeNodeData<U>> value = captor.getValue();
        assertEquals(1, value.size());
        TreeNodeData<U> treeNode = value.get(0);
        assertEquals(A, treeNode.getUserObject());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getNodes_ShouldReturn_OneTreeNodeForEachSuccessorNode() {
        SimpleGraphModel<U> graphModel = SimpleGraphModel.<U>builder()
                .addKeyNode(A)
                .addEdge(A, B)
                .addEdge(A, C)
                .addEdge(A, D)
                .build();
        final GraphTreeNodeModel<U> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getTreeNodesForUserObject(A, nodes -> model.getNodes(Optional.<TreeNodeId>of(nodes.get(0).getId()), callback));
        verify(callback, times(1)).handleNodes(captor.capture());
        List<TreeNodeData<U>> value = captor.getValue();
        assertEquals(3, value.size());
        Set<U> userObjects = Sets.newHashSet(value.get(0).getUserObject(),
                value.get(1).getUserObject(),
                value.get(2).getUserObject());
        assertTrue(userObjects.contains(B));
        assertTrue(userObjects.contains(C));
        assertTrue(userObjects.contains(D));
    }

    @Test

    public void getTreeNodesForUserObject_ShouldReturn_TreeNode() {
        SimpleGraphModel<U> graphModel = SimpleGraphModel.<U>builder()
                .addKeyNode(A)
                .addEdge(A, B)
                .addEdge(B, C)
                .addEdge(C, D)
                .addEdge(D, E)
                .build();
        GraphTreeNodeModel<U> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getTreeNodesForUserObject(C, callback);
        verify(callback, times(1)).handleNodes(captor.capture());
        List<TreeNodeData<U>> nodes = captor.getValue();
        assertEquals(1, nodes.size());
        assertEquals(C, nodes.get(0).getUserObject());
    }

    @Test
    public void getTreeNodesForUserObject_WithMultiplePredecessors_ShouldReturn_TreeNodesForEachPredecessor() {
        SimpleGraphModel<U> graphModel = SimpleGraphModel.<U>builder()
                .addKeyNode(A)
                .addEdge(A, B)
                .addEdge(A, C)
                .addEdge(C, D)
                .addEdge(B, D)
                .build();
        GraphTreeNodeModel<U> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getTreeNodesForUserObject(D, callback);
        verify(callback, times(1)).handleNodes(captor.capture());
        List<TreeNodeData<U>> nodes = captor.getValue();
        assertEquals(2, nodes.size());
        assertEquals(D, nodes.get(0).getUserObject());
        assertEquals(D, nodes.get(1).getUserObject());
    }

    @Test
    public void getTreeNodesForUserObject_WithObjectInCycle_ShouldReturn_ASingleTreeNode() {
        SimpleGraphModel<U> graphModel = SimpleGraphModel.<U>builder()
                .addKeyNode(A)
                .addEdge(A, B)
                .addEdge(A, C)
                .addEdge(A, D)
                .build();
        GraphTreeNodeModel<U> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getTreeNodesForUserObject(D, callback);
        verify(callback, times(1)).handleNodes(captor.capture());
        List<TreeNodeData<U>> nodes = captor.getValue();
        assertEquals(1, nodes.size());
        assertEquals(D, nodes.get(0).getUserObject());
    }
}
