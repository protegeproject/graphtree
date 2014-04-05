package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class TreeNodeViewTraverser<U extends Serializable> {

    private final Path.Transform<TreeNodeView<U>,U>  view2UserObjectTransform = new Path.Transform<TreeNodeView<U>, U>() {
        @Override
        public U transform(TreeNodeView<U> element) {
            return element.getUserObject();
        }
    };

    public static <U extends Serializable> TreeNodeViewTraverser<U> newTreeNodeViewTraverser() {
        return new TreeNodeViewTraverser<U>();
    }

    public Optional<TreeNodeView<U>> getPrevious(TreeNodeView<U> treeNodeView) {
        Optional<TreeNodeView<U>> ancestor = getFirstVisibleAncestor(treeNodeView);
        if(!ancestor.isPresent()) {
            return Optional.absent();
        }
        final Optional<TreeNodeView<U>> parentView = treeNodeView.getParentView();
        if (ancestor.equals(parentView)) {
            Optional<TreeNodeView<U>> previousSibling = treeNodeView.getPreviousSibling();
            if(previousSibling.isPresent()) {
                return Optional.<TreeNodeView<U>>of(getDeepestLastChild(previousSibling.get()));
            }
        }
        return ancestor;
    }

    public Iterator<TreeNodeView<U>> iterator(TreeNodeView<U> fromView) {
        return new TreeNodeViewIterator<U>(fromView);
    }

    public Optional<TreeNodeView<U>> getFirstVisibleAncestor(final TreeNodeView<U> fromView) {
        Path<TreeNodeView<U>> pathToRoot = getPathToRoot(fromView);
        if(pathToRoot.size() < 2) {
            return Optional.absent();
        }
        for(int i = 0; i < pathToRoot.size() - 2; i++) {
            TreeNodeView<U> view = pathToRoot.get(i);
            if(!view.isExpanded()) {
                return Optional.of(view);
            }
        }
        return Optional.of(pathToRoot.get(pathToRoot.size() - 2));
    }

    public Path<TreeNodeView<U>> getPathToRoot(final TreeNodeView<U> fromView) {
        return getPathToRoot(fromView, Path.<TreeNodeView<U>>getIdentityTransform());
    }

    public <T> Path<T> getPathToRoot(final TreeNodeView<U> fromView, Path.Transform<TreeNodeView<U>, T> transform) {
        List<T> pathToRoot = Lists.newArrayList();
        Optional<TreeNodeView<U>> v = Optional.of(fromView);
        while(v.isPresent()) {
            pathToRoot.add(0, transform.transform(v.get()));
            v = v.get().getParentView();
        }
        return new Path<T>(pathToRoot);
    }

    public Path<U> getUserObjectPathToRoot(final TreeNodeView<U> fromView) {
        return getPathToRoot(fromView, view2UserObjectTransform);
    }

    public Path<TreeNodeId> getTreeNodePathToRoot(final TreeNodeView<U> fromView) {
        return getPathToRoot(fromView, new Path.Transform<TreeNodeView<U>, TreeNodeId>() {
            @Override
            public TreeNodeId transform(TreeNodeView<U> element) {
                return element.getNodeId();
            }
        });
    }

    public Optional<TreeNodeView<U>> getNext(TreeNodeView<U> treeNodeView) {
        Optional<TreeNodeView<U>> ancestor = getFirstVisibleAncestor(treeNodeView);
        if (treeNodeView.getParentView().equals(ancestor)) {
            if(!treeNodeView.isEmpty() && treeNodeView.isExpanded()) {
                return treeNodeView.getFirstChildView();
            }
            Optional<TreeNodeView<U>> nextSibling = treeNodeView.getNextSibling();
            if(nextSibling.isPresent()) {
               return nextSibling;
            }
        }
        while(ancestor.isPresent()) {
            Optional<TreeNodeView<U>> nextParentSibling = ancestor.get().getNextSibling();
            if(nextParentSibling.isPresent()) {
                return nextParentSibling;
            }
            ancestor = ancestor.get().getParentView();
        }
        return Optional.absent();
    }

    private TreeNodeView<U> getDeepestLastChild(TreeNodeView<U> fromView) {
        if(fromView.isCollapsed()) {
            return fromView;
        }
        Optional<TreeNodeView<U>> lastChildView = fromView.getLastChildView();
        if(lastChildView.isPresent()) {
            return getDeepestLastChild(lastChildView.get());
        }
        return fromView;
    }




    private class TreeNodeViewIterator<U extends Serializable> implements Iterator<TreeNodeView<U>> {

        private Queue<TreeNodeView<U>> queue = Lists.newLinkedList();

        private TreeNodeViewIterator(TreeNodeView<U> fromView) {
            queue.add(fromView);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public TreeNodeView<U> next() {
            TreeNodeView<U> view = queue.poll();
            for(TreeNodeView<U> childView : view.getChildViews()) {
                queue.add(childView);
            }
            return view;
        }

        @Override
        public void remove() {
        }
    }
}
