package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.DropType;
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

    interface DropEndHandler {

        void handleDropComplete();

        void handleDropCancelled();
    }

    /**
     * Handle the drop of the dragged node on the specified path.
     * @param draggedNode The path to the dragged node.
     * @param dropTarget The path to the target node (the parent node).  This may be an empty path.
     * @param dropType The type of drop.
     * @param handler A handler to handle the drop complete.
     */
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
