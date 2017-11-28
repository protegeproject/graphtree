package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.resources.client.CssResource;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/01/2014
 */
public interface TreeNodeViewCss extends CssResource {

    @ClassName("tree-row")
    String row();

    @ClassName("tree-row--selected")
    String rowSelected();

    @ClassName("tree-row--drag-over")
    String rowDragOver();

    @ClassName("tree-row__handle")
    String handle();
}
