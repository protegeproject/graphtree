package edu.stanford.protege.gwt.graphtree.shared.tree;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public interface TreeNodeModelChangeVisitor<U extends Serializable> {

    void visit(RootNodeAdded<U> rootNodeAdded);

    void visit(RootNodeRemoved<U> rootNodeRemoved);

    void visit(ChildNodeAdded<U> childNodeAdded);

    void visit(ChildNodeRemoved<U> childNodeRemoved);

    void visit(NodeUserObjectChanged<U> nodeUserObjectChanged);
}
