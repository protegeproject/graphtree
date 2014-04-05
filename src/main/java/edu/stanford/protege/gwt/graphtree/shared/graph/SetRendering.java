package edu.stanford.protege.gwt.graphtree.shared.graph;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class SetRendering<U extends Serializable> extends GraphModelChange<U> {

    private GraphNode<U> graphNode;

    private String html;

    private SetRendering() {
    }

    public SetRendering(GraphNode<U> graphNode, String html) {
        this.graphNode = graphNode;
        this.html = html;
    }

    public GraphNode<U> getGraphNode() {
        return graphNode;
    }

    public String getHtml() {
        return html;
    }

    @Override
    public GraphModelChange<U> getReverseChange() {
        return new SetRendering<U>(graphNode, html);
    }

    @Override
    public void accept(GraphModelChangeVisitor<U> visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "SetRendering".hashCode() + graphNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof SetRendering)) {
            return false;
        }
        SetRendering other = (SetRendering) o;
        return this.graphNode.equals(other.graphNode);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("SetRendering").addValue(graphNode).addValue(html).toString();
    }
}
