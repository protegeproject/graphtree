package edu.stanford.protege.gwt.graphtree.client;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class TreeNodeViewNextSiblingFinder<U extends Serializable> {

    private final TreeNodeView<U> view;

    public TreeNodeViewNextSiblingFinder(TreeNodeView<U> view) {
        this.view = view;
    }

    public Optional<TreeNodeView> getNextSibling() {
        Optional<TreeNodeView<U>> parent = view.getParentView();
        if(!parent.isPresent()) {
            return Optional.empty();
        }
        Iterator<TreeNodeView<U>> childIterator = parent.get().getChildViews().iterator();
        while(childIterator.hasNext()) {
            TreeNodeView childView = childIterator.next();
            if(childView == view) {
                if(childIterator.hasNext()) {
                    return Optional.of(childIterator.next());
                }
                else {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }
}
