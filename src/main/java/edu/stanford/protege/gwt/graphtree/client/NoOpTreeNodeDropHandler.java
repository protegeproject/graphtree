package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.DropType;
import edu.stanford.protege.gwt.graphtree.shared.Path;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
public class NoOpTreeNodeDropHandler<U extends Serializable> implements TreeNodeDropHandler<U> {

    @Override
    public void handleDrop(@Nonnull Path<U> draggedNode, @Nonnull Path<U> dropTarget, @Nonnull DropType dropType, @Nonnull DropEndHandler handler) {
    }

    @Override
    public void handleTextDrop(@Nonnull String draggedText, @Nonnull Path<U> dropTarget, @Nonnull DropType dropType, @Nonnull DropEndHandler handler) {
    }

    @Override
    public boolean isDropPossible(@Nonnull Path<U> draggedNode, @Nonnull Path<U> dropTargetPath, @Nonnull DropType dropType) {
        return false;
    }

    @Override
    public boolean isTextDropPossible(@Nonnull Path<U> dropTargetPath, @Nonnull DropType dropType) {
        return false;
    }
}
