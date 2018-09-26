package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.protege.gwt.graphtree.client.SelectionChangeEvent.SelectionChangeHandler;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public interface SelectionModel {

    void clearSelection();

    void extendSelection(@Nonnull TreeNodeId id);

    void setSelected(@Nonnull TreeNodeId treeNodeId);

    void setSelected(@Nonnull Stream<TreeNodeId> treeNodeIds);

    void setSelected(@Nonnull Collection<TreeNodeId> treeNodeIds);

    ImmutableSet<TreeNodeId> getSelection();

    boolean isSelected(@Nonnull TreeNodeId id);

    void toggleSelection(@Nonnull TreeNodeId id);

    HandlerRegistration addSelectionChangeHandler(SelectionChangeHandler o);
}
