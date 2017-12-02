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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeNodeIndex_Root_TestCase<U extends Serializable> {

    @Mock
    private TreeNodeData<U> A;

    @Mock
    private U userObjectA;

    @Mock
    private TreeNodeId idA;

    private final UserObjectKeyProvider<U, String> keyProvider = Object::toString;

    private TreeNodeIndex<U, String> index;

    @Before
    public void setUp() throws Exception {
        index = new TreeNodeIndex<>(keyProvider);
        when(A.getId()).thenReturn(idA);
        when(A.getUserObject()).thenReturn(userObjectA);
    }

    @Test
    public void addRootShouldAddRoot() {
        index.addRoot(A);
        List<TreeNodeData<U>> roots = index.getRoots();
        assertThat(roots.size(), equalTo(1));
        assertThat(roots, hasItem(A));
    }

    @Test
    public void addRootShouldIndexAgainstUserObject() {
        index.addRoot(A);
        List<TreeNodeData<U>> result = index.getTreeNodesForUserObjectKey(userObjectA.toString());
        assertThat(result.size(), equalTo(1));
        assertThat(result, hasItem(A));
    }

    @Test
    public void addRootShouldIndexAgainstTreeNodeId() {
        index.addRoot(A);
        Optional<TreeNodeData<U>> result = index.getTreeNodeData(idA);
        assertThat(result, equalTo(Optional.of(A)));
    }

    @Test
    public void removeRootShouldRemoveIndexAgainstUserObject() {
        index.addRoot(A);
        assertThat(index.getTreeNodesForUserObjectKey(userObjectA.toString()), hasItem(A));
        index.removeRoot(A.getId());
        assertThat(index.getTreeNodesForUserObjectKey(userObjectA.toString()), is(empty()));
    }

    @Test
    public void removeRootShouldRemoveIndexAgainstTreeNodeId() {
        index.addRoot(A);
        assertThat(index.getTreeNodeData(idA), equalTo(Optional.of(A)));
        index.removeRoot(idA);
        assertThat(index.getTreeNodeData(idA), equalTo(Optional.empty()));
    }
}
