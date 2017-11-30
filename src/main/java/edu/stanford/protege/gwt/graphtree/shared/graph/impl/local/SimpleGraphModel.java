package edu.stanford.protege.gwt.graphtree.shared.graph.impl.local;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.UserObjectKeyProvider;
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
public class SimpleGraphModel<U extends Serializable, K> implements GraphModel<U, K>, HasSuccessors<GraphNode<U>> {

    private final UserObjectKeyProvider<U, K> keyProvider;

    private final Set<GraphNode<U>> keyNodes = Sets.newLinkedHashSet();

    private final Multimap<K, K> successorMap = LinkedHashMultimap.create();

    private final Map<K, U> key2UserObject = new HashMap<>();

    private EventBus eventBus;

    private SimpleGraphModel(@Nonnull final UserObjectKeyProvider<U, K> keyProvider,
                             @Nonnull final Set<U> keyNodeUserObjects,
                             @Nonnull final Multimap<U, U> edgeMap,
                             @Nonnull final EventBus eventBus) {
        this.keyProvider = checkNotNull(keyProvider);
        this.eventBus = checkNotNull(eventBus);
        for (U keyNodeUserObject : checkNotNull(keyNodeUserObjects)) {
            boolean sink = edgeMap.get(keyNodeUserObject).isEmpty();
            GraphNode<U> keyNode = GraphNode.get(keyNodeUserObject, sink);
            key2UserObject.put(keyProvider.getKey(keyNodeUserObject), keyNodeUserObject);
            keyNodes.add(keyNode);
        }
        edgeMap.entries().forEach(entry -> {
            U predecessorUserObject = entry.getKey();
            U successorUserObject = entry.getValue();
            K predecessorKey = keyProvider.getKey(predecessorUserObject);
            K successorKey = keyProvider.getKey(successorUserObject);
            key2UserObject.put(predecessorKey, predecessorUserObject);
            key2UserObject.put(successorKey, successorUserObject);
            successorMap.put(predecessorKey, successorKey);
        });
    }

    public static <U extends Serializable, K> SimpleGraphModel<U, K> create(@Nonnull UserObjectKeyProvider<U, K> userObjectKeyProvider,
                                                                      @Nonnull Set<U> keyNodes,
                                                                      @Nonnull Multimap<U, U> successorMap) {
        return new SimpleGraphModel<>(userObjectKeyProvider, keyNodes, successorMap, new SimpleEventBus());
    }

    public static <U extends Serializable, K> GraphModelBuilder<U, K> builder(@Nonnull UserObjectKeyProvider<U, K> keyProvider) {
        return new GraphModelBuilder<>(keyProvider);
    }

    public Iterator<GraphEdge> iterator() {
        return new Iterator<GraphEdge>() {
            private Iterator<Map.Entry<K, K>> iterator = successorMap.entries().iterator();

            public boolean hasNext() {
                return iterator().hasNext();
            }

            public GraphEdge next() {
                Map.Entry<K, K> entry = iterator.next();
                U predecessorUserObject = key2UserObject.get(entry.getKey());
                return new GraphEdge<>(GraphNode.get(predecessorUserObject, false),
                                       getGraphNode(entry.getValue()));
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public void getRootNodes(GetRootNodesCallback<U> callback) {
        callback.handleRootNodes(new ArrayList<>(keyNodes));
    }

    @Override
    public void getSuccessorNodes(@Nonnull K predecessorUserObjectKey,
                                  @Nonnull GetSuccessorNodesCallback<U> callback) {
        SuccessorMap.Builder<U> builder = SuccessorMap.builder();
        final Collection<K> successorKeys = successorMap.get(predecessorUserObjectKey);
        if (!successorKeys.isEmpty()) {
            for (K successorKey : successorKeys) {
                builder.add(getGraphNode(predecessorUserObjectKey), getGraphNode(successorKey));
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
    public void getPathsFromRootNodes(@Nonnull K toUserObjectKey,
                                      @Nonnull GetPathsBetweenNodesCallback<U> callback) {
        List<Path<GraphNode<U>>> result = new ArrayList<>();
        for (GraphNode<U> keyNode : keyNodes) {
            PathFinder<GraphNode<U>> pathFinder = new PathFinder<>(this);
            Collection<Path<GraphNode<U>>> paths = pathFinder.getPaths(keyNode, getGraphNode(toUserObjectKey));
            result.addAll(paths);
        }
        callback.handlePaths(result);
    }

    @Override
    public Iterable<GraphNode<U>> getSuccessors(GraphNode<U> node) {
        List<GraphNode<U>> result = new ArrayList<>();
        K predecessorKey = keyProvider.getKey(node.getUserObject());
        for (K successorKey : successorMap.get(predecessorKey)) {
            U userObject = key2UserObject.get(successorKey);
            result.add(GraphNode.get(userObject));
        }
        return result;
    }

    public HandlerRegistration addGraphModelHandler(GraphModelChangedHandler<U> handler) {
        return eventBus.addHandler(GraphModelChangedEvent.<U>getType(), handler);
    }

    public void addEdge(U predecessor, U successor) {
        K predecessorKey = keyProvider.getKey(predecessor);
        K successorKey = keyProvider.getKey(successor);
        if (successorMap.put(predecessorKey, successorKey)) {
            GraphEdge<U> edge = new GraphEdge<>(GraphNode.get(predecessor, false), getGraphNode(successorKey));
            GraphModelChangedEvent.fire(eventBus, new AddEdge<>(edge));
        }
    }

    public void removeEdge(U predecessor, U successor) {
        K predecessorKey = keyProvider.getKey(predecessor);
        K successorKey = keyProvider.getKey(successor);
        if (successorMap.remove(predecessorKey, successorKey)) {
            GraphEdge<U> edge = new GraphEdge<>(GraphNode.get(predecessor, false), getGraphNode(successorKey));
            GraphModelChangedEvent.fire(eventBus, new RemoveEdge<>(edge));
        }
    }

    private GraphNode<U> getGraphNode(K key) {
        U userObject = key2UserObject.get(key);
        return GraphNode.get(userObject, isSink(key));
    }

    private boolean isSink(K key) {
        return successorMap.get(key).isEmpty();
    }

    public static class GraphModelBuilder<U extends Serializable, K> {

        private final UserObjectKeyProvider<U, K> keyProvider;

        private Set<U> keyNodes = Sets.newLinkedHashSet();

        private Multimap<U, U> successorMap = LinkedHashMultimap.create();

        private EventBus eventBus = new SimpleEventBus();

        public void setEventBus(EventBus eventBus) {
            this.eventBus = eventBus;
        }

        public GraphModelBuilder(@Nonnull UserObjectKeyProvider<U, K> keyProvider) {
            this.keyProvider = checkNotNull(keyProvider);
        }

        public GraphModelBuilder<U, K> addRootNode(U userObject) {
            keyNodes.add(userObject);
            return this;
        }

        public GraphModelBuilder<U, K> addEdge(U from, U to) {
            successorMap.put(from, to);
            return this;
        }

        public SimpleGraphModel<U, K> build() {
            return new SimpleGraphModel<>(keyProvider, keyNodes, successorMap, eventBus);
        }
    }
}
