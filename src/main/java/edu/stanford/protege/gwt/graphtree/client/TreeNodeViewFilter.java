package edu.stanford.protege.gwt.graphtree.client;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/01/2014
 */
public interface  TreeNodeViewFilter {

    boolean isIncluded(TreeNodeView view);
}
