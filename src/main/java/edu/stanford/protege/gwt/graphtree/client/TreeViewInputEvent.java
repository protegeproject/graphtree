package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.KeyEvent;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/01/2014
 */
public class TreeViewInputEvent<U extends Serializable> {

    public static final TreeViewInputEvent EMPTY_EVENT = new TreeViewInputEvent(false, false, false);

    private final boolean altDown;

    private final boolean ctrlDown;

    private final boolean shiftDown;

    public TreeViewInputEvent(boolean altDown, boolean ctrlDown, boolean shiftDown) {
        this.altDown = altDown;
        this.ctrlDown = ctrlDown;
        this.shiftDown = shiftDown;
    }

    @SuppressWarnings("unchecked")
    public static <U extends Serializable> TreeViewInputEvent<U> empty() {
        return (TreeViewInputEvent<U>) EMPTY_EVENT;
    }

    public static <U extends Serializable> Builder<U> builder() {
        return new Builder<U>();
    }

    public boolean isAltDown() {
        return altDown;
    }

    public boolean isCtrlDown() {
        return ctrlDown;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public static <U extends Serializable> TreeViewInputEvent<U> fromEvent(KeyEvent<?> event) {
        Builder<U> builder = builder();
        builder.setAltDown(event.isAltKeyDown());
        builder.setCtrlDown(event.isControlKeyDown());
        builder.setShiftDown(event.isShiftKeyDown());
        return builder.build();
    }

    public static <U extends Serializable> TreeViewInputEvent<U> fromEvent(HumanInputEvent<?> event) {
        Builder<U> builder = builder();
        builder.setAltDown(event.isAltKeyDown());
        builder.setCtrlDown(event.isControlKeyDown());
        builder.setShiftDown(event.isShiftKeyDown());
        return builder.build();
    }

    public static class Builder<U extends Serializable> {

        private boolean altDown;

        private boolean ctrlDown;

        private boolean shiftDown;

        public Builder setAltDown(boolean altDown) {
            this.altDown = altDown;
            return this;
        }

        public Builder setCtrlDown(boolean ctrlDown) {
            this.ctrlDown = ctrlDown;
            return this;
        }

        public Builder setShiftDown(boolean shiftDown) {
            this.shiftDown = shiftDown;
            return this;
        }


        public TreeViewInputEvent<U> build() {
            return new TreeViewInputEvent<U>(altDown, ctrlDown, shiftDown);
        }
    }
}
