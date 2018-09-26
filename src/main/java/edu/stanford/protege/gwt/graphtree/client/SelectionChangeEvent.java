package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class SelectionChangeEvent {

    public interface SelectionChangeHandler {
        void handleSelectionChange(@Nonnull SelectionChangeEvent event);
    }
}
