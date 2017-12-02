package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/01/2014
 */
public class GraphModelChangeTidier<U extends Serializable> {

    private final List<GraphModelChange<U>> changes;

    public GraphModelChangeTidier(List<GraphModelChange<U>> changes) {
        this.changes = new ArrayList<>(changes);
    }

    public List<GraphModelChange<U>> getTidiedChanges() {
        final Set<GraphEdge<U>> addedEdges = Sets.newHashSet();
        final Set<GraphEdge<U>> removedEdges = Sets.newHashSet();
        final Set<GraphNode<U>> addedKeyNodes = Sets.newHashSet();
        final Set<GraphNode<U>> removedKeyNodes = Sets.newHashSet();
        final Set<UpdateUserObject<U>> setRenderingChanges = Sets.newHashSet();
        for(GraphModelChange<U> change : changes) {
            change.accept(new GraphModelChangeVisitor<U>() {
                public void visit(AddRootNode<U> addRootNode) {
                    if(!removedKeyNodes.remove(addRootNode.getNode())) {
                        addedKeyNodes.add(addRootNode.getNode());
                    }
                }

                public void visit(RemoveRootNode<U> removeRootNode) {
                    if(!addedKeyNodes.remove(removeRootNode.getNode())) {
                        removedKeyNodes.add(removeRootNode.getNode());
                    }
                }

                public void visit(AddEdge<U> addEdge) {
                    GraphEdge<U> edge = addEdge.getEdge();
                    if(!removedEdges.remove(edge)) {
                        addedEdges.add(edge);
                    }
                }

                public void visit(RemoveEdge<U> removeEdge) {
                    GraphEdge<U> edge = removeEdge.getEdge();
                    if(!addedEdges.remove(edge)) {
                        removedEdges.add(edge);
                    }
                }

                public void visit(UpdateUserObject<U> updateUserObject) {
                    setRenderingChanges.add(updateUserObject);
                }
            });
        }
        List<GraphModelChange<U>> result = new ArrayList<>();
        for(GraphNode<U> node : addedKeyNodes) {
            result.add(new AddRootNode<>(node));
        }
        for(GraphNode<U> node : removedKeyNodes) {
            result.add(new RemoveRootNode<>(node));
        }
        for(GraphEdge<U> edge : getTopologicallyOrderedEdges(addedEdges, TopologicalSorter.Direction.FORWARD)) {
            result.add(new AddEdge<>(edge));
        }
        for(GraphEdge<U> edge : getTopologicallyOrderedEdges(removedEdges, TopologicalSorter.Direction.REVERSE)) {
            result.add(new RemoveEdge<>(edge));
        }
        result.addAll(setRenderingChanges);
        return result;
    }


    private Iterable<GraphEdge<U>> getTopologicallyOrderedEdges(Collection<GraphEdge<U>> edges, TopologicalSorter.Direction direction) {
        Multimap<GraphNode<U>, GraphNode<U>> predecessor2SuccessorMap = HashMultimap.create();
        for(GraphEdge<U> edge : edges) {
            predecessor2SuccessorMap.put(edge.getPredecessor(), edge.getSuccessor());
        }
        TopologicalSorter<U> sorter = new TopologicalSorter<>(predecessor2SuccessorMap);
        final Optional<List<GraphNode<U>>> sortedNodes = sorter.getTopologicalOrdering(direction);
        if(!sortedNodes.isPresent()) {
            return edges;
        }
        else {
            List<GraphEdge<U>> topologicallySortedEdges = new ArrayList<>();
            for(GraphNode<U> predecessorNode : sortedNodes.get()) {
                for(GraphNode<U> successorNode : predecessor2SuccessorMap.get(predecessorNode)){
                    topologicallySortedEdges.add(new GraphEdge<>(predecessorNode, successorNode));
                }
            }
            return topologicallySortedEdges;
        }
    }
}
