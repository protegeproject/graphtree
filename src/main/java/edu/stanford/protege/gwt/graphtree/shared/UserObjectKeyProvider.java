package edu.stanford.protege.gwt.graphtree.shared;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 *
 * A function that provides a key for a user object.
 */
@FunctionalInterface
public interface UserObjectKeyProvider<U> {

    /**
     * Gets a key for the given user object.  Repeated calls for a given object will always
     * return the same key.
     * @param userObject The user object.
     * @return The key for the user object.
     */
    @Nonnull
    Object getKey(@Nonnull U userObject);
}
