package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class graphtree implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        TreeNodeViewResources.RESOURCES.style().ensureInjected();
    }

}
