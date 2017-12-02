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

    private final Set<PendingChangeHandler> pending = new HashSet<PendingChangeHandler>();

    private final SetSelectionModel<TreeNode<U>> selectionModel;

    private final HasSetTreeNodeExpanded hasSetTreeNodeExpanded;

    public PendingChangesManager(HasSetTreeNodeExpanded hasSetTreeNodeExpanded,
                                 SetSelectionModel<TreeNode<U>> selectionModel) {
        this.hasSetTreeNodeExpanded = hasSetTreeNodeExpanded;
        this.selectionModel = selectionModel;
    }

    @Override
    public void handleTreeNodeModelEvent(@Nonnull TreeNodeModelEvent event) {
        for(TreeNodeModelChange change : event.getChanges()) {
            for(Iterator<PendingChangeHandler> it = pending.iterator(); it.hasNext();) {
                PendingChangeHandler changeHandler = it.next();
                if(changeHandler.handle(change)) {
                    it.remove();
                }
            }
//            handleChangedNodes(change.getTreeNodes());
        }
    }

//    private void handleChangedNodes(Set<TreeNode> nodes) {
//        for(Iterator<PendingChangeHandler> it = pending.iterator(); it.hasNext(); ) {
//            PendingChangeHandler handler = it.next();
//            TreeNodeView view = handler.getView();
//            if(nodes.contains(view.getNodeId())) {
//                handler.end();
//                it.remove();
//            }
//        }
//    }

    public enum FinalExpansionState {
        SET_EXPANDED,
        DO_NOTHING
    }

    public enum FinalSelectionState {
        SET_SELECTED,
        DO_NOTHING
    }

    private void addPendingChangeHandler(PendingChangeHandler handler) {
        pending.add(handler);
        handler.begin();
    }

    @Override
    public void setChildAdditionPending(TreeNodeView parentView) {
        addPendingChangeHandler(new PendingChildAdditionHandler(parentView, hasSetTreeNodeExpanded, selectionModel));
    }

    @Override
    public void setRemovalPending(TreeNodeView removedView) {
        addPendingChangeHandler(new PendingRemovalHandler(removedView));
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

        public abstract boolean handle(TreeNodeModelChange change);
    }


    private static class PendingChildAdditionHandler<U extends Serializable> extends PendingChangeHandler<U> {

        private FinalExpansionState expansionState;

        private FinalSelectionState selectionState;

        private final HasSetTreeNodeExpanded hasSetTreeNodeExpanded;

        private final SetSelectionModel<TreeNodeId> selectionModel;

        private PendingChildAdditionHandler(TreeNodeView<U> view,
                                            HasSetTreeNodeExpanded hasSetTreeNodeExpanded,
                                            SetSelectionModel<TreeNodeId> selectionModel) {
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

        }

        @Override
        public boolean handle(TreeNodeModelChange change) {
            if(change instanceof ChildNodeAdded && ((ChildNodeAdded) change).getParentNode().equals(getView().getNodeId())) {
                end();
                getView().setLoadingIndicatorDisplayed(false);
                hasSetTreeNodeExpanded.setTreeNodeExpanded(getView().getNodeId());
                selectionModel.setSelected(((ChildNodeAdded) change).getChildNode().getId(), true);
                return true;
            }
            else {
                return false;
            }
        }
    }


    private static class PendingRemovalHandler extends PendingChangeHandler {

        private PendingRemovalHandler(TreeNodeView view) {
            super(view);
        }

        @Override
        public void begin() {
            getView().asWidget().getElement().getStyle().setOpacity(0.3);
        }

        @Override
        public void end() {
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
