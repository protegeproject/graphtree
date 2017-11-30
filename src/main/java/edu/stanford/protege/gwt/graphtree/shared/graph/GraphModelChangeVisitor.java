package edu.stanford.protege.gwt.graphtree.shared.graph;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public interface GraphModelChangeVisitor<U extends Serializable> {

    void visit(AddRootNode<U> addRootNode);

    void visit(RemoveRootNode<U> removeRootNode);

    void visit(AddEdge<U> addEdge);

    void visit(RemoveEdge<U> removeEdge);

    void visit(UpdateUserObject<U> updateUserObject);
}
