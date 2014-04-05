package edu.stanford.protege.gwt.graphtree.client;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public enum TreeNodeViewState {

    EXPANDED,

    COLLAPSED;

    /**
     * Gets this state's next state.  If the state is {@link #EXPANDED} then the next state will be {@link #COLLAPSED}.
     * If this state is {@link #COLLAPSED} then the next state will be {@link #EXPANDED}.
     * @return The next state.  Not {@code null}.
     */
    public TreeNodeViewState getNextState() {
        switch (this) {
            case EXPANDED:
                return COLLAPSED;
            case COLLAPSED:
                return EXPANDED;
            default:
                throw new RuntimeException("Implementation Error.  Unrecognized value: " + this);
        }
    }
}
