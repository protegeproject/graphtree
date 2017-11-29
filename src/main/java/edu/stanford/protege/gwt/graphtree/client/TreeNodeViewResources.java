package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/01/2014
 */

public interface TreeNodeViewResources extends ClientBundle {

    TreeNodeViewResources RESOURCES =GWT.create(TreeNodeViewResources.class);

    @Source("treenodeview.css")
    TreeNodeViewCss style();

    @Source("collapsed.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource collapsed();

    @Source("expanded.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource expanded();

    @Source("loading.svg")
    @DataResource.MimeType("image/svg+xml")
    DataResource loading();


}
