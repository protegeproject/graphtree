package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.Lists;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyIterator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/01/2014
 */
public class TreeNodeViewTraverser<U extends Serializable> {

    private final Path.Transform<TreeNodeView<U>, U> view2UserObjectTransform = TreeNodeView::getUserObject;

    public static <U extends Serializable> TreeNodeViewTraverser<U> newTreeNodeViewTraverser() {
        return new TreeNodeViewTraverser<>();
    }

    public Optional<TreeNodeView<U>> getPrevious(TreeNodeView<U> treeNodeView) {
        Optional<TreeNodeView<U>> ancestor = getFirstVisibleAncestor(treeNodeView);
        //        if(!ancestor.isPresent()) {
        //            return Optional.empty();
        //        }
        final Optional<TreeNodeView<U>> parentView = treeNodeView.getParentView();
        if (ancestor.equals(parentView)) {
            Optional<TreeNodeView<U>> previousSibling = treeNodeView.getPreviousSibling();
            if (previousSibling.isPresent()) {
                return Optional.of(getDeepestLastChild(previousSibling.get()));
            }
        }
        return ancestor;
    }

    public Iterator<TreeNodeView<U>> iterator(TreeNodeView<U> fromView) {
        return new TreeNodeViewIterator<>(fromView);
    }

    public Optional<TreeNodeView<U>> getFirstVisibleAncestor(final TreeNodeView<U> fromView) {
        Path<TreeNodeView<U>> pathToRoot = getPathToRoot(fromView);
        if (pathToRoot.size() < 2) {
            return Optional.empty();
        }
        for (int i = 0; i < pathToRoot.size() - 2; i++) {
            TreeNodeView<U> view = pathToRoot.get(i);
            if (!view.isExpanded()) {
                return Optional.of(view);
            }
        }
        return Optional.of(pathToRoot.get(pathToRoot.size() - 2));
    }

    public Path<TreeNodeView<U>> getPathToRoot(final TreeNodeView<U> fromView) {
        return getPathToRoot(fromView, Path.getIdentityTransform());
    }

    public <T> Path<T> getPathToRoot(final TreeNodeView<U> fromView, Path.Transform<TreeNodeView<U>, T> transform) {
        List<T> pathToRoot = Lists.newArrayList();
        Optional<TreeNodeView<U>> v = Optional.of(fromView);
        while (v.isPresent()) {
            pathToRoot.add(0, transform.transform(v.get()));
            v = v.get().getParentView();
        }
        return new Path<>(pathToRoot);
    }

    public Path<U> getUserObjectPathToRoot(final TreeNodeView<U> fromView) {
        return getPathToRoot(fromView, view2UserObjectTransform);
    }

    public Path<TreeNodeId> getTreeNodePathToRoot(final TreeNodeView<U> fromView) {
        return getPathToRoot(fromView, TreeNodeView::getNodeId);
    }

    public Optional<TreeNodeView<U>> getNext(TreeNodeView<U> treeNodeView) {
        Optional<TreeNodeView<U>> ancestor = getFirstVisibleAncestor(treeNodeView);
        if (treeNodeView.getParentView().equals(ancestor)) {
            if (!treeNodeView.isEmpty() && treeNodeView.isExpanded()) {
                return treeNodeView.getFirstChildView();
            }
            Optional<TreeNodeView<U>> nextSibling = treeNodeView.getNextSibling();
            if (nextSibling.isPresent()) {
                return nextSibling;
            }
        }
        while (ancestor.isPresent()) {
            Optional<TreeNodeView<U>> nextParentSibling = ancestor.get().getNextSibling();
            if (nextParentSibling.isPresent()) {
                return nextParentSibling;
            }
            ancestor = ancestor.get().getParentView();
        }
        return Optional.empty();
    }

    private TreeNodeView<U> getDeepestLastChild(TreeNodeView<U> fromView) {
        if (fromView.isCollapsed()) {
            return fromView;
        }
        Optional<TreeNodeView<U>> lastChildView = fromView.getLastChildView();
        return lastChildView.map(this::getDeepestLastChild).orElse(fromView);
    }


    public List<TreeNodeView<U>> getVisibleViewsBetween(@Nonnull TreeNodeView<U> from,
                                                        @Nonnull TreeNodeView<U> to) {
        Iterator<TreeNodeView<U>> iterator = getPathToRoot(from)
                .getFirst()
                .map(tnv -> (Iterator<TreeNodeView<U>>) new VisibleTreeNodeViewIterator(tnv))
                .orElse(Collections.emptyIterator());
        List<TreeNodeView<U>> result = new ArrayList<>();
        boolean inList = false;
        while (iterator.hasNext()) {
            TreeNodeView<U> tnv = iterator.next();
            if (tnv.equals(from) || tnv.equals(to)) {
                result.add(tnv);
                if (inList) {
                    break;
                }
                else {
                    inList = true;
                }
            }
            else if (inList) {
                result.add(tnv);
            }
        }
        if (!result.isEmpty() && result.get(0).equals(to)) {
            Collections.reverse(result);
        }
        return result;
    }


    private static class TreeNodeViewIterator<K extends Serializable> implements Iterator<TreeNodeView<K>> {

        private final Queue<TreeNodeView<K>> queue = Lists.newLinkedList();

        private TreeNodeViewIterator(TreeNodeView<K> fromView) {
            queue.add(fromView);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public TreeNodeView<K> next() {
            TreeNodeView<K> view = queue.poll();
            for (TreeNodeView<K> childView : view.getChildViews()) {
                queue.add(childView);
            }
            return view;
        }

        @Override
        public void remove() {
        }
    }

    private class VisibleTreeNodeViewIterator implements Iterator<TreeNodeView<U>> {

        @Nonnull
        private Optional<TreeNodeView<U>> view;

        private VisibleTreeNodeViewIterator(@Nonnull TreeNodeView<U> fromView) {
            this.view = Optional.of(checkNotNull(fromView));
        }

        @Override
        public boolean hasNext() {
            return view.isPresent();
        }

        @Override
        public TreeNodeView<U> next() {
            if (view.isPresent()) {
                TreeNodeView<U> cur = view.get();
                view = TreeNodeViewTraverser.this.getNext(cur);
                return cur;
            }
            else {
                throw new NoSuchElementException();
            }
        }
    }
}
