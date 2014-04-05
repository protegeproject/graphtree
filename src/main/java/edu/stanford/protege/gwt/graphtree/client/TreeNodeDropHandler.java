package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/01/2014
 */
public interface TreeNodeDropHandler<U extends Serializable> {

    public static enum  DropType {
        MOVE,
        ADD
    }

    public static interface DropEndHandler {

        void handleDropComplete();

        void handleDropCancelled();
    }

    void handleDrop(Path<U> draggedNode, Path<U> dropTarget, DropType dropType, DropEndHandler handler);

    void handleTextDrop(String draggedText, Path<U> dropTarget, DropType dropType, DropEndHandler handler);

    boolean isDropPossible(Path<U> draggedNode, Path<U> dropTargetPath, DropType dropType);

    boolean isTextDropPossible(Path<U> dropTargetPath, DropType dropType);
}
