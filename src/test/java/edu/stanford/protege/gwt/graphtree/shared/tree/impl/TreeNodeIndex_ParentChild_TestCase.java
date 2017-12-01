package edu.stanford.protege.gwt.graphtree.shared.tree.impl;

import com.google.common.collect.Multimap;
import edu.stanford.protege.gwt.graphtree.shared.UserObjectKeyProvider;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeData;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeNodeIndex_ParentChild_TestCase<U extends Serializable> {

    @Mock
    private TreeNodeData<U> child;

    @Mock
    private U childObject;

    @Mock
    private TreeNodeId parentId, childId;

    @Mock
    private Multimap<TreeNodeId, TreeNodeId> removedBranches;

    private UserObjectKeyProvider<U, String> keyProvider = Object::toString;

    private TreeNodeIndex<U, String> index;

    @Before
    public void setUp() throws Exception {
        index = new TreeNodeIndex<>(keyProvider);
        when(child.getId()).thenReturn(childId);
        when(child.getUserObject()).thenReturn(childObject);
    }

    @Test
    public void addChildShouldAddChild() {
        index.addChild(parentId, child);
        assertThat(index.getChildren(parentId), hasItem(child));
    }

    @Test
    public void addChildShouldAddIndexToParent() {
        index.addChild(parentId, child);
        assertThat(index.getParent(childId), equalTo(Optional.of(parentId)));
    }

    @Test
    public void addChildShouldIndexAgainstUserObject() {
        index.addChild(parentId, child);
        assertThat(index.getTreeNodesForUserObjectKey(childObject.toString()), hasItem(child));
    }

    @Test
    public void addChildShouldIndexAgainstTreeNodeId() {
        index.addChild(parentId, child);
        assertThat(index.getTreeNodeData(childId), equalTo(child));
    }

    @Test
    public void removeChildShouldRemoveChild() {
        index.addChild(parentId, child);
        assertThat(index.getTreeNodeData(childId), equalTo(child));
        index.removeChild(parentId, childId, removedBranches);
        verify(removedBranches).put(parentId, childId);
        assertThat(index.getChildren(parentId), is(empty()));
    }

    @Test
    public void removeChildShouldRemoveIndexAgainstUserObject() {
        index.addChild(parentId, child);
        assertThat(index.getTreeNodeData(childId), equalTo(child));
        index.removeChild(parentId, childId, removedBranches);
        assertThat(index.getTreeNodesForUserObjectKey(childObject.toString()), is(empty()));
    }

    @Test
    public void removeChildShouldRemoveIndexAgainstTreeNodeId() {
        index.addChild(parentId, child);
        assertThat(index.getTreeNodeData(childId), equalTo(child));
        index.removeChild(parentId, childId, removedBranches);
        assertThat(index.getTreeNodeData(childId), nullValue());
    }

    @Test
    public void removeChildShouldRemoveIndexToParent() {
        index.addChild(parentId, child);
        assertThat(index.getTreeNodeData(childId), equalTo(child));
        index.removeChild(parentId, childId, removedBranches);
        assertThat(index.getParent(childId), equalTo(Optional.empty()));
    }

    @Test
    public void shouldContainChildNodeWithUserObject() {
        index.addChild(parentId, child);
        assertThat(index.containsChildWithUserObject(parentId, childObject), is(true));
    }

    @Test
    public void shouldNotContainChildNodeWithUserObject() {
        assertThat(index.containsChildWithUserObject(parentId, childObject), is(false));
    }

}
