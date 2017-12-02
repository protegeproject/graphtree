package edu.stanford.protege.gwt.graphtree.client;

import edu.stanford.protege.gwt.graphtree.shared.Path;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/02/2014
 */
public class NoOpTreeNodeDropHandler<U extends Serializable> implements TreeNodeDropHandler<U> {

    @Override
    public void handleDrop(Path<U> draggedNode, Path<U> dropTarget, DropType dropType, DropEndHandler handler) {
    }

    @Override
    public void handleTextDrop(String draggedText, Path<U> dropTarget, DropType dropType, DropEndHandler handler) {
    }

    @Override
    public boolean isDropPossible(Path<U> draggedNode, Path<U> dropTargetPath, DropType dropType) {
        return false;
    }

    @Override
    public boolean isTextDropPossible(Path<U> dropTargetPath, DropType dropType) {
        return false;
    }
}
