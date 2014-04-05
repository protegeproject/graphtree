package edu.stanford.protege.gwt.graphtree.shared.graph;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/01/2014
 */
public class DepthFirstIterator<N> implements Iterator<N> {

    private HasSuccessors<N> hasSuccessors;

    private List<N> stack;

    private Set<N> visited;

    public DepthFirstIterator(N N, HasSuccessors<N> hasSuccessors) {
        this.hasSuccessors = hasSuccessors;
        stack = new Stack<N>();
        stack.add(N);
        visited = new HashSet<N>();
        visited.add(N);
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public N next() {
        N next = stack.remove(stack.size() - 1);
        int index = stack.size();
        for (N successor : hasSuccessors.getSuccessors(next)) {
            if (!visited.contains(successor)) {
                stack.add(index, successor);
                visited.add(successor);
            }
        }
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
