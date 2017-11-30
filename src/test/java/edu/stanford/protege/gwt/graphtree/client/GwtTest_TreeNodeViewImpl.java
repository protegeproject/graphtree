package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.junit.client.GWTTestCase;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/02/2014
 */
public class GwtTest_TreeNodeViewImpl extends GWTTestCase {

    private static int counter;

    @Override
    public String getModuleName() {
        return "edu.stanford.protege.gwt.graphtree.graphtreeJUnit";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        delayTestFinish(10000);
    }

    private static TreeNodeViewImpl<String> getTreeNodeView() {
        TreeNode<String> node = getTreeNode();
        return new TreeNodeViewImpl<>(node.getId(), node.getUserObject());
    }

    private static TreeNode<String> getTreeNode() {
        counter++;
        return new TreeNode<>(new TreeNodeId(counter), "A");
    }

    public void test_getNodeId_ShouldReturnSuppliedNodeId() {
        TreeNode<String> node = getTreeNode();
        TreeNodeViewImpl view = new TreeNodeViewImpl<>(node.getId(), node.getUserObject());
        assertEquals(node.getId(), view.getNodeId());
        finishTest();
    }

    public void test_isExpanded_ShouldReturnFalseByDefault() {
        assertFalse(getTreeNodeView().isExpanded());
        finishTest();
    }

    public void test_isExpanded_ReturnsTrueAfterSetExpandedIsCalled() {
        TreeNodeViewImpl view = getTreeNodeView();
        view.setExpanded();
        assertTrue(view.isExpanded());
        finishTest();
    }

    public void test_isCollapsed_ReturnsFalseAfterSetExpandedIsCalled() {
        TreeNodeViewImpl view = getTreeNodeView();
        view.setExpanded();
        assertFalse(view.isCollapsed());
        finishTest();
    }

    public void test_isCollapsed_ShouldReturnTrueByDefault() {
        assertTrue(getTreeNodeView().isCollapsed());
        finishTest();
    }

    public void test_isEmpty_ShouldReturnTrueByDefault() {
        assertTrue(getTreeNodeView().isEmpty());
        finishTest();
    }

    public void test_isEmpty_ShouldReturnFalseAfterAddingAChild() {
        TreeNodeViewImpl view = getTreeNodeView();
        view.addChildView(getTreeNodeView());
        assertFalse(view.isEmpty());
        finishTest();
    }

    public void test_isSelected_ShouldReturnFalseByDefault() {
        assertFalse(getTreeNodeView().isSelected());
        finishTest();
    }

    public void test_getDataState_ShouldReturnUnloadedByDefault() {
        TreeNodeViewImpl view = getTreeNodeView();
        assertEquals(DataState.UNLOADED, view.getDataState());
        finishTest();
    }

    public void test_getParentView_ShouldReturn_OptionalemptyByDefault() {
        assertEquals(Optional.<TreeNodeView>empty(), getTreeNodeView().getParentView());
        finishTest();
    }

    public void test_getFirstChildView_ShouldReturn_OptionalemptyByDefault() {
        assertEquals(Optional.<TreeNodeView>empty(), getTreeNodeView().getFirstChildView());
        finishTest();
    }

    public void test_getFirstChildView_ShouldReturnFirstChild() {
        TreeNodeViewImpl parent = getTreeNodeView();
        TreeNodeViewImpl child = getTreeNodeView();
        parent.addChildView(child);
        assertEquals(Optional.<TreeNodeView>of(child), parent.getFirstChildView());
        finishTest();
    }

    public void test_getLastChildView_ShouldReturnLastChild() {
        TreeNodeViewImpl parent = getTreeNodeView();
        TreeNodeViewImpl child = getTreeNodeView();
        parent.addChildView(child);
        assertEquals(Optional.<TreeNodeView>of(child), parent.getLastChildView());
        finishTest();
    }

    public void test_getDataState_Returns_setDataState_Value() {
        TreeNodeViewImpl view = getTreeNodeView();
        view.setDataState(DataState.LOADED);
        assertEquals(DataState.LOADED, view.getDataState());
        finishTest();
    }

    public void test_getLastChildView_ShouldReturn_OptionalemptyByDefault() {
        assertEquals(Optional.<TreeNodeView>empty(), getTreeNodeView().getLastChildView());
        finishTest();
    }

    public void test_getNextSiblingView_ShouldReturn_OptionalemptyByDefault() {
        assertEquals(Optional.<TreeNodeView>empty(), getTreeNodeView().getNextSibling());
        finishTest();
    }

    public void test_getNextSiblingView_ShouldReturnSetNextSibling() {
        TreeNodeViewImpl parentView = getTreeNodeView();
        TreeNodeViewImpl firstChildView = getTreeNodeView();
        TreeNodeViewImpl secondChildView = getTreeNodeView();
        parentView.addChildView(firstChildView);
        parentView.addChildView(secondChildView);
        assertEquals(Optional.<TreeNodeView>of(secondChildView), firstChildView.getNextSibling());
        finishTest();
    }

    public void test_getNextSiblingView_ShouldReturnemptyAfterRemove() {
        TreeNodeViewImpl parentView = getTreeNodeView();
        TreeNodeViewImpl firstChildView = getTreeNodeView();
        TreeNodeViewImpl secondChildView = getTreeNodeView();
        parentView.addChildView(firstChildView);
        parentView.addChildView(secondChildView);
        parentView.removeChildView(secondChildView);
        assertEquals(Optional.<TreeNodeView>empty(), firstChildView.getNextSibling());
        finishTest();
    }

    public void test_getPreviousSiblingView_ShouldReturnSetPreviousSibling() {
        TreeNodeViewImpl parentView = getTreeNodeView();
        TreeNodeViewImpl firstChildView = getTreeNodeView();
        TreeNodeViewImpl secondChildView = getTreeNodeView();
        parentView.addChildView(firstChildView);
        parentView.addChildView(secondChildView);
        assertEquals(Optional.<TreeNodeView>of(firstChildView), secondChildView.getPreviousSibling());
        finishTest();
    }

    public void test_getPreviousSiblingView_ShouldReturnemptyAfterRemove() {
        TreeNodeViewImpl parentView = getTreeNodeView();
        TreeNodeViewImpl firstChildView = getTreeNodeView();
        TreeNodeViewImpl secondChildView = getTreeNodeView();
        parentView.addChildView(firstChildView);
        parentView.addChildView(secondChildView);
        parentView.removeChildView(firstChildView);
        assertEquals(Optional.<TreeNodeView>empty(), secondChildView.getPreviousSibling());
        finishTest();
    }

    public void test_getNextSiblingShouldReturnNextInSpecifiedList() {
        TreeNodeView<String> parentView = getTreeNodeView();
        List<TreeNodeView<String>> childViews = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            childViews.add(getTreeNodeView());
        }
        parentView.setChildViews(childViews);
        assertEquals(Optional.<TreeNodeView>of(childViews.get(1)), childViews.get(0).getNextSibling());
        assertEquals(Optional.<TreeNodeView>of(childViews.get(2)), childViews.get(1).getNextSibling());
        assertEquals(Optional.<TreeNodeView>of(childViews.get(3)), childViews.get(2).getNextSibling());
        assertEquals(Optional.<TreeNodeView>empty(), childViews.get(3).getNextSibling());
        finishTest();
    }

    public void test_getPreviousSiblingShouldReturnPreviousInSpecifiedList() {
        TreeNodeView<String> parentView = getTreeNodeView();
        List<TreeNodeView<String>> childViews = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            childViews.add(getTreeNodeView());
        }
        parentView.setChildViews(childViews);
        assertEquals(Optional.<TreeNodeView>empty(), childViews.get(0).getPreviousSibling());
        assertEquals(Optional.<TreeNodeView>of(childViews.get(0)), childViews.get(1).getPreviousSibling());
        assertEquals(Optional.<TreeNodeView>of(childViews.get(1)), childViews.get(2).getPreviousSibling());
        assertEquals(Optional.<TreeNodeView>of(childViews.get(2)), childViews.get(3).getPreviousSibling());
        finishTest();
    }

    public void test_getPreviousSiblingView_ShouldReturn_OptionalemptyByDefault() {
        assertEquals(Optional.<TreeNodeView>empty(), getTreeNodeView().getPreviousSibling());
        finishTest();
    }

    public void test_getDepth_ReturnsZeroByDefault() {
        TreeNodeViewImpl view = getTreeNodeView();
        assertEquals(0, view.getDepth());
        finishTest();
    }

    public void test_setDepth_Returns_setDepth_Value() {
        TreeNodeViewImpl view = getTreeNodeView();
        view.setDepth(3);
        assertEquals(3, view.getDepth());
        finishTest();
    }

    public void test_getViewState_ShouldReturn_Collapsed_ByDefault() {
        TreeNodeViewImpl view = getTreeNodeView();
        assertEquals(TreeNodeViewState.COLLAPSED, view.getViewState());
        finishTest();
    }

    public void test_getViewState_ShouldReturn_setViewState_Value() {
        TreeNodeViewImpl view = getTreeNodeView();
        view.setViewState(TreeNodeViewState.EXPANDED);
        assertEquals(TreeNodeViewState.EXPANDED, view.getViewState());
        finishTest();
    }


    public void test_getViewState_ShouldReturn_Expanded_AfterCallTo_setExpanded() {
        TreeNodeViewImpl view = getTreeNodeView();
        view.setExpanded();
        assertEquals(TreeNodeViewState.EXPANDED, view.getViewState());
        finishTest();
    }




}
