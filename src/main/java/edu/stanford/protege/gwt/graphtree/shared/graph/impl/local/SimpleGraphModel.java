package edu.stanford.protege.gwt.graphtree.shared.graph.impl.local;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 * <p>
 * A local graph model that can be used for testing purposes
 * </p>
 */
public class SimpleGraphModel<U extends Serializable> implements GraphModel<U>, HasSuccessors<GraphNode<U>> {

    private final Set<GraphNode<U>> keyNodes;

    private final Multimap<U, U> successorMap;

    private EventBus eventBus;

    private SimpleGraphModel(@Nonnull final Set<U> userObjectKeyNodes,
                             @Nonnull final Multimap<U, U> edgeMap,
                             @Nonnull final EventBus eventBus) {
        this.successorMap = LinkedHashMultimap.create();
        this.keyNodes = Sets.newLinkedHashSet();
        this.eventBus = checkNotNull(eventBus);
        for (U userObjectKeyNode : checkNotNull(userObjectKeyNodes)) {
            boolean sink = edgeMap.get(userObjectKeyNode).isEmpty();
            GraphNode<U> keyNode = GraphNode.get(userObjectKeyNode, sink);
            keyNodes.add(keyNode);
        }
        successorMap.putAll(edgeMap);
    }

    public static <U extends Serializable> SimpleGraphModel<U> create(@Nonnull Set<U> keyNodes,
                                                                      @Nonnull Multimap<U, U> successorMap) {
        return new SimpleGraphModel<>(keyNodes, successorMap, new SimpleEventBus());
    }

    public static <U extends Serializable> GraphModelBuilder<U> builder() {
        return new GraphModelBuilder<>();
    }

    public Iterator<GraphEdge> iterator() {
        return new Iterator<GraphEdge>() {
            private Iterator<Map.Entry<U, U>> iterator = successorMap.entries().iterator();

            public boolean hasNext() {
                return iterator().hasNext();
            }

            public GraphEdge next() {
                Map.Entry<U, U> entry = iterator.next();
                return new GraphEdge<>(GraphNode.get(entry.getKey(), false), getGraphNode(entry.getValue()));
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public void getKeyNodes(GetKeyNodesCallback<U> callback) {
        callback.handleKeyNodes(new ArrayList<>(keyNodes));
    }

    @Override
    public void getSuccessorNodes(U userObject, GetSuccessorNodesCallback<U> callback) {
        SuccessorMap.Builder<U> builder = SuccessorMap.builder();
        final Collection<U> successors = successorMap.get(userObject);
        if (!successors.isEmpty()) {
            for (U successorUserObject : successors) {
                builder.add(getGraphNode(userObject), getGraphNode(successorUserObject));
            }
        }
        callback.handleSuccessorNodes(builder.build());
    }

    @Override
    public void getPathsBetweenNodes(U fromUserObject, U toUserObject, GetPathsBetweenNodesCallback<U> callback) {
        PathFinder<GraphNode<U>> pathFinder = new PathFinder<>(this);
        Collection<Path<GraphNode<U>>> paths = pathFinder.getPaths(GraphNode.get(fromUserObject),
                GraphNode.get(toUserObject));
        callback.handlePaths(paths);
    }

    @Override
    public void getPathsFromKeyNodes(U toUserObject, GetPathsBetweenNodesCallback<U> callback) {
        List<Path<GraphNode<U>>> result = new ArrayList<>();
        for (GraphNode<U> keyNode : keyNodes) {
            PathFinder<GraphNode<U>> pathFinder = new PathFinder<>(this);
            Collection<Path<GraphNode<U>>> paths = pathFinder.getPaths(keyNode, GraphNode.get(toUserObject));
            result.addAll(paths);
        }
        callback.handlePaths(result);
    }

    @Override
    public Iterable<GraphNode<U>> getSuccessors(GraphNode<U> node) {
        List<GraphNode<U>> result = new ArrayList<>();
        for (U userObject : successorMap.get(node.getUserObject())) {
            result.add(GraphNode.get(userObject));
        }
        return result;
    }

    public HandlerRegistration addGraphModelHandler(GraphModelChangedHandler<U> handler) {
        return eventBus.addHandler(GraphModelChangedEvent.<U>getType(), handler);
    }

    public void addEdge(U predecessor, U successor) {
        if (successorMap.put(predecessor, successor)) {
            final boolean sink = isSink(successor);
            GraphEdge<U> edge = new GraphEdge<>(GraphNode.get(predecessor, false), getGraphNode(successor));
            GraphModelChangedEvent.fire(eventBus, new AddEdge<>(edge));
        }
    }

    public void removeEdge(U predecessor, U successor) {
        if (successorMap.remove(predecessor, successor)) {
            GraphEdge<U> edge = new GraphEdge<>(GraphNode.get(predecessor, false), getGraphNode(successor));
            GraphModelChangedEvent.fire(eventBus, new RemoveEdge<>(edge));
        }
    }

    private GraphNode<U> getGraphNode(U userObject) {
        return GraphNode.get(userObject, isSink(userObject));
    }

    private boolean isSink(U successor) {
        return successorMap.get(successor).isEmpty();
    }

    public static class GraphModelBuilder<U extends Serializable> {

        private Set<U> keyNodes = Sets.newLinkedHashSet();

        private Multimap<U, U> successorMap = LinkedHashMultimap.create();

        private EventBus eventBus = new SimpleEventBus();

        public void setEventBus(EventBus eventBus) {
            this.eventBus = eventBus;
        }

        public GraphModelBuilder<U> addKeyNode(U userObject) {
            keyNodes.add(userObject);
            return this;
        }

        public GraphModelBuilder<U> addEdge(U from, U to) {
            successorMap.put(from, to);
            return this;
        }

        public SimpleGraphModel<U> build() {
            return new SimpleGraphModel<>(keyNodes, successorMap, eventBus);
        }
    }
}
