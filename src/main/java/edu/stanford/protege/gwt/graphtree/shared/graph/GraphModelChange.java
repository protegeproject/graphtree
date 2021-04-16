package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
@JsonSubTypes({
        @Type(EdgeChange.class),
        @Type(AddRootNode.class),
        @Type(UpdateUserObject.class),
        @Type(RemoveRootNode.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class GraphModelChange<U extends Serializable> implements Serializable, IsSerializable {

    public GraphModelChange() {
    }

    public abstract void accept(GraphModelChangeVisitor<U> visitor);

    abstract void forEachGraphNode(Consumer<GraphNode<U>> nodeConsumer);
}
