package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;

import java.util.Iterator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class TreeNodeViewNextSiblingFinder {

    private TreeNodeView view;

    public TreeNodeViewNextSiblingFinder(TreeNodeView view) {
        this.view = view;
    }

    public Optional<TreeNodeView> getNextSibling() {
        Optional<TreeNodeView> parent = view.getParentView();
        if(!parent.isPresent()) {
            return Optional.absent();
        }
        Iterator<TreeNodeView> childIterator = parent.get().getChildViews().iterator();
        while(childIterator.hasNext()) {
            TreeNodeView childView = childIterator.next();
            if(childView == view) {
                if(childIterator.hasNext()) {
                    return Optional.of(childIterator.next());
                }
                else {
                    return Optional.absent();
                }
            }
        }
        return Optional.absent();
    }
}
