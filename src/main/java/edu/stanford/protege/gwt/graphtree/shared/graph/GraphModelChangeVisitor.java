package edu.stanford.protege.gwt.graphtree.shared.graph;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public interface GraphModelChangeVisitor<U extends Serializable> {

    void visit(AddKeyNode<U> addKeyNode);

    void visit(RemoveKeyNode<U> removeKeyNode);

    void visit(AddEdge<U> addEdge);

    void visit(RemoveEdge<U> removeEdge);

    void visit(SetRendering<U> setRendering);
}
