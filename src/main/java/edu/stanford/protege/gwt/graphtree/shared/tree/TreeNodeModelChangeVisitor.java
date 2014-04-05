package edu.stanford.protege.gwt.graphtree.shared.tree;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public interface TreeNodeModelChangeVisitor {

    void visit(RootNodeAdded rootNodeAdded);

    void visit(RootNodeRemoved rootNodeRemoved);

    void visit(ChildNodeAdded childNodeAdded);

    void visit(ChildNodeRemoved childNodeRemoved);

    void visit(NodeRenderingChanged nodeRenderingChanged);
}
