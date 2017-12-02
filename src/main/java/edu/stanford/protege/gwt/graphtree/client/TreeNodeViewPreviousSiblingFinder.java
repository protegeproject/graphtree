package edu.stanford.protege.gwt.graphtree.client;

import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class TreeNodeViewPreviousSiblingFinder<U extends Serializable> {

    private final TreeNodeView<U> view;

    public TreeNodeViewPreviousSiblingFinder(TreeNodeView<U> view) {
        this.view = view;
    }

    public Optional<TreeNodeView<U>> getPreviousSibling() {
        Optional<TreeNodeView<U>> parent = view.getParentView();
        if (!parent.isPresent()) {
            return Optional.empty();
        }
        TreeNodeView<U> previousSibling = null;
        for(TreeNodeView<U> childView : parent.get().getChildViews()) {
            if(childView == view) {
                return Optional.ofNullable(previousSibling);
            }
            previousSibling = childView;
        }
        return Optional.empty();
    }
}
