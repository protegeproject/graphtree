package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.user.client.Window;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Dec 2017
 */
public class Platform {

    public static Platform get() {
        return new Platform();
    }

    public boolean isMacOS() {
        return Window.Navigator.getPlatform().toLowerCase().contains("mac");
    }
}
