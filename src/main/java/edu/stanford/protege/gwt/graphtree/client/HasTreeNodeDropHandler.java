package edu.stanford.protege.gwt.graphtree.client;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public interface HasTreeNodeDropHandler<U extends Serializable> {

    void setDropHandler(TreeNodeDropHandler<U> dropHandler);
}
