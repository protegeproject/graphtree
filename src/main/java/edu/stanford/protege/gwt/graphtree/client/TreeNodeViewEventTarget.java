package edu.stanford.protege.gwt.graphtree.client;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public class TreeNodeViewEventTarget<U extends Serializable> {

    public enum ViewTargetType {
        HANDLE,
        CONTENT
    }

    private final TreeNodeView<U> view;

    private final ViewTargetType viewHandleIsTarget;

    public TreeNodeViewEventTarget(TreeNodeView<U> view, ViewTargetType viewHandleIsTarget) {
        this.view = view;
        this.viewHandleIsTarget = viewHandleIsTarget;
    }

    public TreeNodeView<U> getView() {
        return view;
    }

    public boolean isViewHandleTarget() {
        return viewHandleIsTarget == ViewTargetType.HANDLE;
    }
}
