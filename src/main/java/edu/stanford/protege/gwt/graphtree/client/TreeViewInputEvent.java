package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.KeyEvent;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 30/01/2014
 */
public class TreeViewInputEvent<U extends Serializable> {

    public static final TreeViewInputEvent EMPTY_EVENT = new TreeViewInputEvent(false, false, false, false);

    private final boolean altDown;

    private final boolean ctrlDown;

    private final boolean shiftDown;

    private final boolean metaDown;

    public TreeViewInputEvent(boolean altDown, boolean ctrlDown, boolean shiftDown, boolean metaDown) {
        this.altDown = altDown;
        this.ctrlDown = ctrlDown;
        this.shiftDown = shiftDown;
        this.metaDown = metaDown;
    }

    @SuppressWarnings("unchecked")
    public static <U extends Serializable> TreeViewInputEvent<U> empty() {
        return (TreeViewInputEvent<U>) EMPTY_EVENT;
    }

    public static <U extends Serializable> Builder<U> builder() {
        return new Builder<>();
    }

    public static <U extends Serializable> TreeViewInputEvent<U> fromEvent(KeyEvent<?> event) {
        return Builder.<U>builder()
                .setAltDown(event.isAltKeyDown())
                .setCtrlDown(event.isControlKeyDown())
                .setShiftDown(event.isShiftKeyDown())
                .setMetaDown(event.isMetaKeyDown())
                .build();
    }

    public static <U extends Serializable> TreeViewInputEvent<U> fromEvent(HumanInputEvent<?> event) {
        return Builder.<U>builder()
                .setAltDown(event.isAltKeyDown())
                .setCtrlDown(event.isControlKeyDown())
                .setShiftDown(event.isShiftKeyDown())
                .setMetaDown(event.isMetaKeyDown())
                .build();
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

    public boolean isMetaDown() {
        return metaDown;
    }

    public static class Builder<U extends Serializable> {

        private boolean altDown;

        private boolean ctrlDown;

        private boolean shiftDown;

        private boolean metaDown;

        public static <U extends Serializable> Builder<U> builder() {
            return new Builder<>();
        }

        public Builder<U> setAltDown(boolean altDown) {
            this.altDown = altDown;
            return this;
        }

        public Builder<U> setCtrlDown(boolean ctrlDown) {
            this.ctrlDown = ctrlDown;
            return this;
        }

        public Builder<U> setShiftDown(boolean shiftDown) {
            this.shiftDown = shiftDown;
            return this;
        }

        public Builder<U> setMetaDown(boolean metaDown) {
            this.metaDown = metaDown;
            return this;
        }

        public TreeViewInputEvent<U> build() {
            return new TreeViewInputEvent<>(altDown, ctrlDown, shiftDown, metaDown);
        }
    }
}
