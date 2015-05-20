package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class TreeWidget<U extends Serializable> extends Composite {

    private TreePresenter<U> treePresenter;

    public TreeWidget(TreeView treeView, TreeNodeModel<U> model) {
        SingleSelectionModel<TreeNode<U>> selectionModel = new SingleSelectionModel<TreeNode<U>>();
        treePresenter = new TreePresenter<U>(treeView, selectionModel);
        treePresenter.setModel(model);
        initWidget(treeView.asWidget());
    }

    public TreeWidget(TreeNodeModel<U> model) {
        this(new TreeViewImpl(), model);
    }

    public void setModel(TreeNodeModel<U> model) {
        treePresenter.setModel(model);
    }

    public void setRootNodesExpanded() {
        treePresenter.setRootNodesExpanded();
    }

    public void setDropHandler(TreeNodeDropHandler<U> dropHandler) {
        treePresenter.setDropHandler(dropHandler);
    }

    public HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler) {
        return treePresenter.addSelectionChangeHandler(handler);
    }

    public Path<TreeNodeId> getPathToRoot(TreeNodeId fromNode) {
        return treePresenter.getPathToRoot(fromNode);
    }

    public void clearSelection() {
        treePresenter.clearSelection();
    }

    public Set<TreeNode<U>> getSelectedSet() {
        return treePresenter.getSelectedSet();
    }

    public boolean isSelected(TreeNode<U> object) {
        return treePresenter.isSelected(object);
    }

    public void setSelected(TreeNode<U> object, boolean selected) {
        treePresenter.setSelected(object, selected);
    }

    public Object getKey(TreeNode<U> item) {
        return treePresenter.getKey(item);
    }

    public void setSelected(Object key) {
        treePresenter.setSelected(key);
    }

    public void clearPruning() {
        treePresenter.clearPruning();
    }

    public void pruneToNodes(Collection<TreeNodeId> treeNodes) {
        treePresenter.pruneToNodes(treeNodes);
    }

    public void showTreeNodesForUserObject(U userObject) {
        treePresenter.showTreeNodesForUserObject(userObject);
    }
}
