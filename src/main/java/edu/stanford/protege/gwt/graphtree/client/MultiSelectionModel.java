package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent.SelectionChangeHandler;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MultiSelectionModel implements SelectionModel {

    @Nonnull
    private ImmutableSet<TreeNodeId> selection = ImmutableSet.of();

    @Nonnull
    private List<SelectionChangeHandler> handlers = new ArrayList<>();

    private void setSelectionAndFireEvent(@Nonnull ImmutableSet<TreeNodeId> nodeIds) {
        if(!selection.equals(nodeIds)) {
            selection = nodeIds;
            SelectionChangeEvent event = new SelectionChangeEvent();
            handlers.forEach(h -> h.handleSelectionChange(event));
        }
    }

    @Override
    public void clearSelection() {
        setSelectionAndFireEvent(ImmutableSet.of());
    }

    @Override
    public void extendSelection(@Nonnull TreeNodeId id) {
        ImmutableSet.Builder<TreeNodeId> builder = builderFromSelection();
        builder.add(id);
        setSelectionAndFireEvent(builder.build());
    }

    private ImmutableSet.Builder<TreeNodeId> builderFromSelection() {
        ImmutableSet.Builder<TreeNodeId> builder = ImmutableSet.builder();
        builder.addAll(getSelection());
        return builder;
    }

    @Override
    public void setSelected(@Nonnull TreeNodeId treeNodeId) {
        setSelectionAndFireEvent(ImmutableSet.of(treeNodeId));
    }

    @Override
    public void setSelected(@Nonnull Stream<TreeNodeId> treeNodeIds) {
        setSelectionAndFireEvent(treeNodeIds.collect(toImmutableSet()));
    }

    @Override
    public void setSelected(@Nonnull Collection<TreeNodeId> treeNodeIds) {
        setSelectionAndFireEvent(ImmutableSet.copyOf(treeNodeIds));
    }

    @Nonnull
    @Override
    public ImmutableSet<TreeNodeId> getSelection() {
        return selection;
    }

    @Override
    public boolean isSelected(@Nonnull TreeNodeId id) {
        return selection.contains(id);
    }

    @Override
    public void toggleSelection(@Nonnull TreeNodeId id) {
        ImmutableSet<TreeNodeId> nextSel;
        if(selection.contains(id)) {
            nextSel = selection.stream().filter(n -> !n.equals(id)).collect(toImmutableSet());
        }
        else {
            nextSel = builderFromSelection().add(id).build();
        }
        setSelectionAndFireEvent(nextSel);
    }

    @Override
    public HandlerRegistration addSelectionChangeHandler(SelectionChangeHandler o) {
        handlers.add(o);
        return () -> handlers.remove(o);
    }
}
