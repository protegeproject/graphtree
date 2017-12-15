package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public abstract class GraphModelChange<U extends Serializable> implements Serializable, IsSerializable {

    public GraphModelChange() {
    }

    public abstract void accept(GraphModelChangeVisitor<U> visitor);

    abstract void forEachGraphNode(Consumer<GraphNode<U>> nodeConsumer);
}
