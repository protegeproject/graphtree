package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.Path;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/01/2014
 */
public interface TreeNodeDropHandler<U extends Serializable> {

    enum  DropType {
        MOVE,
        ADD
    }

    interface DropEndHandler {

        void handleDropComplete();

        void handleDropCancelled();
    }

    void handleDrop(@Nonnull Path<U> draggedNode,
                    @Nonnull Path<U> dropTarget,
                    @Nonnull DropType dropType,
                    @Nonnull DropEndHandler handler);

    void handleTextDrop(@Nonnull String draggedText,
                        @Nonnull Path<U> dropTarget,
                        @Nonnull DropType dropType,
                        @Nonnull DropEndHandler handler);

    boolean isDropPossible(@Nonnull Path<U> draggedNode,
                           @Nonnull Path<U> dropTargetPath,
                           @Nonnull DropType dropType);

    boolean isTextDropPossible(@Nonnull Path<U> dropTargetPath,
                               @Nonnull DropType dropType);
}
