package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.Multimap;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/01/2014
 */
public class TopologicalSorter<U extends Serializable> {

    private final Multimap<GraphNode<U>, GraphNode<U>> graph;

    public enum Direction {

        FORWARD,

        REVERSE
    }

    public TopologicalSorter(Multimap<GraphNode<U>, GraphNode<U>> graph) {
        this.graph = graph;
    }

    public Optional<List<GraphNode<U>>> getTopologicalOrdering(Direction direction) {
        Optional<List<GraphNode<U>>> ordering = getTopologicalOrdering();
        if(!ordering.isPresent()) {
            return ordering;
        }
        if(direction == Direction.REVERSE) {
            Collections.reverse(ordering.get());
        }
        return ordering;
    }

    public Optional<List<GraphNode<U>>> getTopologicalOrdering() {
        List<GraphNode<U>> result = new ArrayList<>();
        Set<GraphNode<U>> marked = new HashSet<>();
        Set<GraphNode<U>> tempMarked = new HashSet<>();
        for (GraphNode<U> node : graph.keySet()) {
            if (!marked.contains(node)) {
                tempMarked.clear();
                if (!visit(node, marked, tempMarked, result)) {
                    return Optional.empty();
                }
            }
        }
        return Optional.<List<GraphNode<U>>>of(result);
    }

    private boolean visit(GraphNode<U> n, Set<GraphNode<U>> marked, Set<GraphNode<U>> tempMark, List<GraphNode<U>> result) {
        if (tempMark.contains(n)) {
            // Not a DAG - cannot sort topologically
            return false;
        }
        tempMark.add(n);
        if (!marked.contains(n)) {
            for (GraphNode<U> m : graph.get(n)) {
                if(!visit(m, marked, tempMark, result)) {
                    return false;
                }
            }
            marked.add(n);
            result.add(0, n);
        }
        return true;
    }

}
