package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/02/2014
 */
public class SuccessorMap<U extends Serializable> implements Serializable {

    private ImmutableMultimap<GraphNode<U>, GraphNode<U>> successorMap;

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
        return new Builder<U>();
    }

    public static class Builder<U extends Serializable> implements Serializable {

        private transient Multimap<GraphNode<U>, GraphNode<U>> successorMap;

        public Builder() {
            this.successorMap = LinkedHashMultimap.create();
        }

        public Builder<U> add(GraphNode<U> predecessor, GraphNode<U> successor) {
            successorMap.put(predecessor, successor);
            return this;
        }

        public SuccessorMap<U> build() {
            return new SuccessorMap<U>(ImmutableMultimap.copyOf(successorMap));
        }

        public Builder<U> sort() {
            for(GraphNode<U> predecessor : new ArrayList<GraphNode<U>>(successorMap.keySet())) {
                List<GraphNode<U>> successors = new ArrayList<GraphNode<U>>(successorMap.get(predecessor));
                Collections.sort(successors);
                successorMap.replaceValues(predecessor, successors);
            }
            return this;
        }

    }
}
