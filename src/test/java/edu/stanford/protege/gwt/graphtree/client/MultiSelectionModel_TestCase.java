package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent.SelectionChangeHandler;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class MultiSelectionModel_TestCase {

    @Mock
    private TreeNodeId treeNodeId, treeNodeId2;

    private MultiSelectionModel selectionModel;

    @Mock
    private SelectionChangeHandler handler;

    @Before
    public void setUp() {
        selectionModel = new MultiSelectionModel();
        selectionModel.addSelectionChangeHandler(handler);
    }

    @Test
    public void shouldSetTreeNodeIdSelected() {
        selectionModel.setSelected(treeNodeId);
        assertThat(selectionModel.getSelection(), is(Collections.singleton(treeNodeId)));
    }

    @Test
    public void shouldFireEventOnSetTreeNodeIdSelected() {
        selectionModel.setSelected(treeNodeId);
        verify(handler, times(1)).handleSelectionChange(any());
    }

    @Test
    public void shouldNotFireEventOnSetTreeNodeIdSelectedWithDuplicate() {
        selectionModel.setSelected(treeNodeId);
        selectionModel.setSelected(treeNodeId);
        verify(handler, times(1)).handleSelectionChange(any());
    }

    @Test
    public void shouldClearSelection() {
        selectionModel.setSelected(treeNodeId);
        reset(handler);
        selectionModel.clearSelection();
        assertThat(selectionModel.getSelection(), is(empty()));
    }

    @Test
    public void shouldFireEventOnClearSelection() {
        selectionModel.setSelected(treeNodeId);
        reset(handler);
        selectionModel.clearSelection();
        verify(handler, times(1)).handleSelectionChange(any());
    }

    @Test
    public void shouldSetSetSelected() {
        ImmutableSet<TreeNodeId> sel = ImmutableSet.of(treeNodeId, treeNodeId2);
        selectionModel.setSelected(sel);
        assertThat(selectionModel.getSelection(), is(sel));
    }

    @Test
    public void shouldExtendSelection() {
        selectionModel.setSelected(treeNodeId);
        assertThat(selectionModel.getSelection(), is(Collections.singleton(treeNodeId)));
        selectionModel.extendSelection(treeNodeId2);
        assertThat(selectionModel.getSelection(), is(ImmutableSet.of(treeNodeId, treeNodeId2)));
    }

    @Test
    public void shouldNotBeSelected() {
        assertThat(selectionModel.isSelected(treeNodeId), is(false));
    }

    @Test
    public void shouldBeSelected() {
        selectionModel.setSelected(treeNodeId);
        assertThat(selectionModel.isSelected(treeNodeId), is(true));
    }

    @Test
    public void shouldToggleSelection() {
        selectionModel.toggleSelection(treeNodeId);
        assertThat(selectionModel.getSelection(), is(Collections.singleton(treeNodeId)));
        selectionModel.toggleSelection(treeNodeId);
        assertThat(selectionModel.getSelection(), is(empty()));
    }
}
