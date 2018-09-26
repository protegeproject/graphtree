package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent.SelectionChangeHandler;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class SingleSelectionModel implements SelectionModel {

    private TreeNodeId selection = null;

    private List<SelectionChangeHandler> handlers = new ArrayList<>();

    @Override
    public void clearSelection() {
        if(!Objects.equals(selection, null)) {
            setSelectionAndFireEvent(null);
        }
    }

    private void setSelectionAndFireEvent(@Nullable TreeNodeId nodeId) {
        if(!Objects.equals(selection, checkNotNull(nodeId))) {
            selection = nodeId;
            SelectionChangeEvent event = new SelectionChangeEvent();
            handlers.forEach(h -> h.handleSelectionChange(event));
        }

    }

    @Override
    public void extendSelection(@Nonnull TreeNodeId id) {
        setSelectionAndFireEvent(id);
    }

    @Override
    public void setSelected(@Nonnull TreeNodeId treeNodeId) {
        setSelectionAndFireEvent(treeNodeId);
    }

    @Override
    public void setSelected(@Nonnull Stream<TreeNodeId> treeNodeIds) {
        setSelectionAndFireEvent(treeNodeIds.findFirst().orElse(null));
    }

    @Override
    public void setSelected(@Nonnull Collection<TreeNodeId> treeNodeIds) {
        setSelected(treeNodeIds.stream());
    }

    @Override
    public ImmutableSet<TreeNodeId> getSelection() {
        if(selection == null) {
            return ImmutableSet.of();
        }
        else {
            return ImmutableSet.of(selection);
        }
    }

    @Override
    public boolean isSelected(@Nonnull TreeNodeId id) {
        return Objects.equals(selection, id);
    }

    @Override
    public void toggleSelection(@Nonnull TreeNodeId id) {
        if(isSelected(id)) {
            selection = null;
        }
        else {
            selection = id;
        }
    }

    @Override
    public HandlerRegistration addSelectionChangeHandler(SelectionChangeHandler o) {
        handlers.add(o);
        return () -> handlers.remove(o);
    }
}
