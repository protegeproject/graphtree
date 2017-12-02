package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.*;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/02/2014
 */
public class SuccessorMap<U extends Serializable> implements Serializable {

    private ImmutableMultimap<GraphNode<U>, GraphNode<U>> successorMap;

    @SuppressWarnings("unused")
    private SuccessorMap() {
    }

    private SuccessorMap(ImmutableMultimap<GraphNode<U>, GraphNode<U>> successorMap) {
        this.successorMap = successorMap;
    }

    public Collection<GraphNode<U>> getSuccessors() {
        return successorMap.values();
    }

    public int size() {
        return successorMap.keySet().size();
    }

    public boolean isEmpty() {
        return successorMap.isEmpty();
    }

    public static <U extends Serializable> Builder<U> builder() {
        return new Builder<>();
    }

    public static class Builder<U extends Serializable> implements Serializable {

        private final transient Multimap<GraphNode<U>, GraphNode<U>> successorMap;

        public Builder() {
            this.successorMap = LinkedHashMultimap.create();
        }

        public Builder<U> add(GraphNode<U> predecessor, GraphNode<U> successor) {
            successorMap.put(predecessor, successor);
            return this;
        }

        public SuccessorMap<U> build() {
            return new SuccessorMap<>(ImmutableMultimap.copyOf(successorMap));
        }

        public Builder<U> sort(Comparator<GraphNode<U>> comparator) {
            for(GraphNode<U> predecessor : new ArrayList<>(successorMap.keySet())) {
                List<GraphNode<U>> successors = new ArrayList<>(successorMap.get(predecessor));
                successors.sort(comparator);
                successorMap.replaceValues(predecessor, successors);
            }
            return this;
        }

    }
}
