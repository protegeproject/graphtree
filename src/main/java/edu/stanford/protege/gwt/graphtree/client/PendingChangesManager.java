package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.view.client.SetSelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.tree.*;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
public class PendingChangesManager<U extends Serializable> implements TreeNodeModelEventHandler, HasPendingChanges<U> {

    private final Set<PendingChangeHandler> pending = new HashSet<>();

    private final SelectionModel selectionModel;

    private final HasSetTreeNodeExpanded hasSetTreeNodeExpanded;

    public PendingChangesManager(HasSetTreeNodeExpanded hasSetTreeNodeExpanded,
                                 SelectionModel selectionModel) {
        this.hasSetTreeNodeExpanded = hasSetTreeNodeExpanded;
        this.selectionModel = selectionModel;
    }

    @Override
    public void handleTreeNodeModelEvent(@Nonnull TreeNodeModelEvent event) {
        for(TreeNodeModelChange change : event.getChanges()) {
            pending.removeIf(changeHandler -> changeHandler.handle(change));
        }
    }

    private void addPendingChangeHandler(PendingChangeHandler<U> handler) {
        pending.add(handler);
        handler.begin();
    }

    @Override
    public void setChildAdditionPending(TreeNodeView<U> parentView) {
        addPendingChangeHandler(new PendingChildAdditionHandler<>(parentView,
                                                                  hasSetTreeNodeExpanded,
                                                                  selectionModel));
    }

    @Override
    public void setRemovalPending(TreeNodeView<U> removedView) {
        addPendingChangeHandler(new PendingRemovalHandler<>(removedView));
    }

    @Override
    public void setRendingChangePending(TreeNodeView view) {
    }

    @Override
    public void setPendingChangedCancelled(TreeNodeView view) {
        for(Iterator<PendingChangeHandler> it = pending.iterator(); it.hasNext(); ) {
            PendingChangeHandler handler = it.next();
            if(handler.getView() == view) {
                handler.end();
                it.remove();
            }
        }
    }


    private static abstract class PendingChangeHandler<U extends Serializable> {

        private final TreeNodeView<U> view;

        protected PendingChangeHandler(TreeNodeView<U> view) {
            this.view = view;
        }

        public TreeNodeView<U> getView() {
            return view;
        }

        public abstract void begin();

        public abstract void end();

        public abstract boolean handle(TreeNodeModelChange<U> change);
    }


    private static class PendingChildAdditionHandler<U extends Serializable> extends PendingChangeHandler<U> {

        private final HasSetTreeNodeExpanded hasSetTreeNodeExpanded;

        private final SelectionModel selectionModel;

        private PendingChildAdditionHandler(TreeNodeView<U> view,
                                            HasSetTreeNodeExpanded hasSetTreeNodeExpanded,
                                            SelectionModel selectionModel) {
            super(view);
            this.hasSetTreeNodeExpanded = hasSetTreeNodeExpanded;
            this.selectionModel = selectionModel;
        }

        @Override
        public void begin() {
            getView().setLoadingIndicatorDisplayed(true);
        }

        @Override
        public void end() {
            getView().setLoadingIndicatorDisplayed(false);
        }

        @Override
        public boolean handle(TreeNodeModelChange<U> change) {
            if(change instanceof ChildNodeAdded && ((ChildNodeAdded) change).getParentNode().equals(getView().getNodeId())) {
                end();
                getView().setLoadingIndicatorDisplayed(false);
                hasSetTreeNodeExpanded.setTreeNodeExpanded(getView().getNodeId());
                TreeNode<U> childNode = ((ChildNodeAdded<U>) change).getChildNode().getTreeNode();
                selectionModel.setSelected(childNode.getId());
                return true;
            }
            else {
                return false;
            }
        }
    }


    private static class PendingRemovalHandler<U extends Serializable> extends PendingChangeHandler<U> {

        private PendingRemovalHandler(TreeNodeView<U> view) {
            super(view);
        }

        @Override
        public void begin() {
            getView().asWidget().getElement().getStyle().setOpacity(0.3);
        }

        @Override
        public void end() {
            getView().asWidget().getElement().getStyle().setOpacity(1.0);
        }

        @Override
        public boolean handle(TreeNodeModelChange change) {
            if(change instanceof ChildNodeRemoved && ((ChildNodeRemoved) change).getChildNode().equals(getView().getNodeId())) {
                getView().asWidget().getElement().getStyle().setOpacity(1.0);
                return true;
            }
            else {
                return false;
            }
        }
    }

}
