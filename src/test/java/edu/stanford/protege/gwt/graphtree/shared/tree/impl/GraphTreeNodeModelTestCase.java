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

    UserObjectKeyProvider<U, String> keyProvider = Object::toString;

    @Test
    public void getNodes_Callback_isCalledForEmptyGraph() {
        SimpleGraphModel<U, String> graphModel = SimpleGraphModel.<U, String>builder(Object::toString).build();
        GraphTreeNodeModel<U, String> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getNodes(Optional.empty(), callback);
        verify(callback, times(1)).handleNodes(captor.capture());
    }

    @Test
    public void getNodes_with_OptionalAbsent_ShouldReturn_RootNodes() {
        SimpleGraphModel<U, String> graphModel = SimpleGraphModel.<U, String>builder(Object::toString).addRootNode(A).build();
        GraphTreeNodeModel<U, String> model = GraphTreeNodeModel.create(graphModel, keyProvider);
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
        SimpleGraphModel<U, String> graphModel = SimpleGraphModel.<U, String>builder(Object::toString)
                .addRootNode(A)
                .addEdge(A, B)
                .addEdge(A, C)
                .addEdge(A, D)
                .build();
        final GraphTreeNodeModel<U, String> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getTreeNodesForUserObjectKey(A.toString(), nodes -> model.getNodes(Optional.<TreeNodeId>of(nodes.get(0).getId()), callback));
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
        SimpleGraphModel<U, String> graphModel = SimpleGraphModel.<U, String>builder(Object::toString)
                .addRootNode(A)
                .addEdge(A, B)
                .addEdge(B, C)
                .addEdge(C, D)
                .addEdge(D, E)
                .build();
        GraphTreeNodeModel<U, String> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getTreeNodesForUserObjectKey(C.toString(), callback);
        verify(callback, times(1)).handleNodes(captor.capture());
        List<TreeNodeData<U>> nodes = captor.getValue();
        assertEquals(1, nodes.size());
        assertEquals(C, nodes.get(0).getUserObject());
    }

    @Test
    public void getTreeNodesForUserObject_WithMultiplePredecessors_ShouldReturn_TreeNodesForEachPredecessor() {
        SimpleGraphModel<U, String> graphModel = SimpleGraphModel.<U, String>builder(Object::toString)
                .addRootNode(A)
                .addEdge(A, B)
                .addEdge(A, C)
                .addEdge(C, D)
                .addEdge(B, D)
                .build();
        GraphTreeNodeModel<U, String> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getTreeNodesForUserObjectKey(D.toString(), callback);
        verify(callback, times(1)).handleNodes(captor.capture());
        List<TreeNodeData<U>> nodes = captor.getValue();
        assertEquals(2, nodes.size());
        assertEquals(D, nodes.get(0).getUserObject());
        assertEquals(D, nodes.get(1).getUserObject());
    }

    @Test
    public void getTreeNodesForUserObject_WithObjectInCycle_ShouldReturn_ASingleTreeNode() {
        SimpleGraphModel<U, String> graphModel = SimpleGraphModel.<U, String>builder(Object::toString)
                .addRootNode(A)
                .addEdge(A, B)
                .addEdge(A, C)
                .addEdge(A, D)
                .build();
        GraphTreeNodeModel<U, String> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getTreeNodesForUserObjectKey(D.toString(), callback);
        verify(callback, times(1)).handleNodes(captor.capture());
        List<TreeNodeData<U>> nodes = captor.getValue();
        assertEquals(1, nodes.size());
        assertEquals(D, nodes.get(0).getUserObject());
    }
}
