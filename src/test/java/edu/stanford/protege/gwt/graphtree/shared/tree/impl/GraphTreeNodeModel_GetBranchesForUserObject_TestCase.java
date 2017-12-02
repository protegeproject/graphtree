package edu.stanford.protege.gwt.graphtree.shared.tree.impl;

import com.google.common.collect.Multimap;
import edu.stanford.protege.gwt.graphtree.shared.UserObjectKeyProvider;
import edu.stanford.protege.gwt.graphtree.shared.graph.impl.local.SimpleGraphModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.HasGetBranches;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import static edu.stanford.protege.gwt.graphtree.shared.graph.TreeNodeMatcher.thatIsATreeNodeWithUserObject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
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
public class GraphTreeNodeModel_GetBranchesForUserObject_TestCase<U extends Serializable> {

    @Mock
    private HasGetBranches.GetBranchesCallback<U> callback;

    @Captor
    private ArgumentCaptor<Multimap<TreeNodeData<U>, TreeNodeData<U>>> captor;

    @Mock
    U A, B, C, D;

    protected final UserObjectKeyProvider<U, String> keyProvider = Object::toString;

    @Test
    public void getBranchesShouldReturnCorrectBranches() {
        SimpleGraphModel<U, String> graphModel = SimpleGraphModel.<U, String>builder(Object::toString)
                .addRootNode(A)
                .addEdge(A, B)
                .build();
        GraphTreeNodeModel<U, String> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getBranchesContainingUserObjectKey(B.toString(), callback);
        verify(callback, times(1)).handleBranches(captor.capture());
        Multimap<TreeNodeData<U>, TreeNodeData<U>> value = captor.getValue();
        assertThat(value.keySet(), hasSize(1));
        assertThat(value.asMap(), hasEntry(thatIsATreeNodeWithUserObject(A), hasItem(thatIsATreeNodeWithUserObject(B))));
    }


    @Test
    public void getBranchesShouldReturnCorrectBranchesForDiamond() {
        SimpleGraphModel<U, String> graphModel = SimpleGraphModel.<U, String>builder(Object::toString)
                .addRootNode(A)
                .addEdge(A, B)
                .addEdge(A, C)
                .addEdge(C, D)
                .addEdge(B, D)
                .build();
        GraphTreeNodeModel<U, String> model = GraphTreeNodeModel.create(graphModel, keyProvider);
        model.getBranchesContainingUserObjectKey(D.toString(), callback);
        verify(callback, times(1)).handleBranches(captor.capture());
        Multimap<TreeNodeData<U>, TreeNodeData<U>> value = captor.getValue();
        Map<TreeNodeData<U>,Collection<TreeNodeData<U>>> theMap = value.asMap();
        assertThat(theMap, hasEntry(thatIsATreeNodeWithUserObject(A), hasItem(thatIsATreeNodeWithUserObject(B))));
        assertThat(theMap, hasEntry(thatIsATreeNodeWithUserObject(A), hasItem(thatIsATreeNodeWithUserObject(C))));
        assertThat(theMap, hasEntry(thatIsATreeNodeWithUserObject(C), hasItem(thatIsATreeNodeWithUserObject(D))));
        assertThat(theMap, hasEntry(thatIsATreeNodeWithUserObject(B), hasItem(thatIsATreeNodeWithUserObject(D))));
        assertEquals(3, value.keySet().size());
    }


}
