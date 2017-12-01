package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.resources.client.CssResource;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/01/2014
 */
public interface TreeNodeViewCss extends CssResource {

    @ClassName("gt-tree")
    String tree();

    @ClassName("gt-tree__row")
    String row();

    @ClassName("gt-tree__row--selected")
    String rowSelected();

    @ClassName("gt-tree__row--drag-over")
    String rowDragOver();

    @ClassName("gt-tree__rendering")
    String rendering();

    @ClassName("gt-tree__handle")
    String handle();

    @ClassName("gt-tree__pruned")
    String pruned();
}
