package edu.stanford.protege.gwt.graphtree.shared.tree.impl;

import edu.stanford.protege.gwt.graphtree.shared.UserObjectKeyProvider;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeNodeIndex_UpdateUserObject_TestCase<U extends Serializable> {

    @Mock
    private TreeNodeData<U> child;

    @Mock
    private U childObject, updatedChildObject;

    @Mock
    private TreeNodeId parentId, childId;

    private final UserObjectKeyProvider<U, String> keyProvider = userObject -> "TheKey";

    private TreeNodeIndex<U, String> index;

    @Before
    public void setUp() throws Exception {
        index = new TreeNodeIndex<>(keyProvider);
        when(child.getId()).thenReturn(childId);
        when(child.getUserObject()).thenReturn(childObject);
    }

    @Test
    public void shouldUpdateUserObjectForParentChild() {
        index.addChild(parentId, child);
        index.updateUserObject(updatedChildObject);
        TreeNodeData<U> updatedData = index.getTreeNodeData(childId);
        assertThat(updatedData != null, is(true));
        assertThat(updatedData.getUserObject(), is(updatedChildObject));
    }

    @Test
    public void shouldUpdateUserObjectForRoot() {
        index.addRoot(child);
        index.updateUserObject(updatedChildObject);
        TreeNodeData<U> updatedData = index.getTreeNodeData(childId);
        assertThat(updatedData != null, is(true));
        assertThat(updatedData.getUserObject(), is(updatedChildObject));
    }

}
