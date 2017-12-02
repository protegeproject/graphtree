package edu.stanford.protege.gwt.graphtree.client;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/05/15
 */
public interface TreeNodeRenderer<U extends Serializable> {

    String getHtmlRendering(U userObject);
}
