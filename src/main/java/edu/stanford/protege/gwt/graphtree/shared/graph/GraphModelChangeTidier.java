package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
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

    private List<GraphModelChange<U>> changes;

    public GraphModelChangeTidier(List<GraphModelChange<U>> changes) {
        this.changes = new ArrayList<GraphModelChange<U>>(changes);
    }

    public List<GraphModelChange<U>> getTidiedChanges() {
        final Set<GraphEdge<U>> addedEdges = Sets.newHashSet();
        final Set<GraphEdge<U>> removedEdges = Sets.newHashSet();
        final Set<GraphNode<U>> addedKeyNodes = Sets.newHashSet();
        final Set<GraphNode<U>> removedKeyNodes = Sets.newHashSet();
        final Map<GraphNode<U>, SetRendering<U>> setRenderingChanges = Maps.newHashMap();
        for(GraphModelChange<U> change : changes) {
            change.accept(new GraphModelChangeVisitor<U>() {
                public void visit(AddKeyNode<U> addKeyNode) {
                    if(!removedKeyNodes.remove(addKeyNode.getNode())) {
                        addedKeyNodes.add(addKeyNode.getNode());
                    }
                }

                public void visit(RemoveKeyNode<U> removeKeyNode) {
                    if(!addedKeyNodes.remove(removeKeyNode.getNode())) {
                        removedKeyNodes.add(removeKeyNode.getNode());
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

                public void visit(SetRendering<U> setRendering) {
                    setRenderingChanges.put(setRendering.getGraphNode(), setRendering);
                }
            });
        }
        List<GraphModelChange<U>> result = new ArrayList<GraphModelChange<U>>();
        for(GraphNode<U> node : addedKeyNodes) {
            result.add(new AddKeyNode<U>(node));
        }
        for(GraphNode<U> node : removedKeyNodes) {
            result.add(new RemoveKeyNode<U>(node));
        }
        for(GraphEdge<U> edge : getTopologicallyOrderedEdges(addedEdges, TopologicalSorter.Direction.FORWARD)) {
            result.add(new AddEdge<U>(edge));
        }
        for(GraphEdge<U> edge : getTopologicallyOrderedEdges(removedEdges, TopologicalSorter.Direction.REVERSE)) {
            result.add(new RemoveEdge<U>(edge));
        }
        result.addAll(setRenderingChanges.values());
        return result;
    }


    private Iterable<GraphEdge<U>> getTopologicallyOrderedEdges(Collection<GraphEdge<U>> edges, TopologicalSorter.Direction direction) {
        Multimap<GraphNode<U>, GraphNode<U>> predecessor2SuccessorMap = HashMultimap.create();
        for(GraphEdge<U> edge : edges) {
            predecessor2SuccessorMap.put(edge.getPredecessor(), edge.getSuccessor());
        }
        TopologicalSorter<U> sorter = new TopologicalSorter<U>(predecessor2SuccessorMap);
        final Optional<List<GraphNode<U>>> sortedNodes = sorter.getTopologicalOrdering(direction);
        if(!sortedNodes.isPresent()) {
            return edges;
        }
        else {
            List<GraphEdge<U>> topologicallySortedEdges = new ArrayList<GraphEdge<U>>();
            for(GraphNode<U> predecessorNode : sortedNodes.get()) {
                for(GraphNode<U> successorNode : predecessor2SuccessorMap.get(predecessorNode)){
                    topologicallySortedEdges.add(new GraphEdge<U>(predecessorNode, successorNode));
                }
            }
            return topologicallySortedEdges;
        }
    }
}
