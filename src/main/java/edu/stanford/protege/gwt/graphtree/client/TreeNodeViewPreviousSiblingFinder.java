package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class TreeNodeViewPreviousSiblingFinder<U extends Serializable> {

    private TreeNodeView<U> view;

    public TreeNodeViewPreviousSiblingFinder(TreeNodeView<U> view) {
        this.view = view;
    }

    public Optional<TreeNodeView<U>> getPreviousSibling() {
        Optional<TreeNodeView<U>> parent = view.getParentView();
        if (!parent.isPresent()) {
            return Optional.absent();
        }
        TreeNodeView<U> previousSibling = null;
        for(TreeNodeView<U> childView : parent.get().getChildViews()) {
            if(childView == view) {
                return Optional.fromNullable(previousSibling);
            }
            previousSibling = childView;
        }
        return Optional.absent();
    }
}
