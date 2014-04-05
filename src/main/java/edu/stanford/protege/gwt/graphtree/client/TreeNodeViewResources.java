package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/01/2014
 */

public interface TreeNodeViewResources extends ClientBundle {

    public static final TreeNodeViewResources RESOURCES =GWT.create(TreeNodeViewResources.class);

    @Source("treenodeview.css")
    TreeNodeViewCss style();
}
