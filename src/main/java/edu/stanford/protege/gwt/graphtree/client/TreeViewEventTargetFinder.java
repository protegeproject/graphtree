package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public class TreeViewEventTargetFinder<U extends Serializable> {

    private final TreeNodeViewManager<U> viewManager;

    public TreeViewEventTargetFinder(TreeNodeViewManager<U> viewManager) {
        this.viewManager = viewManager;
    }

    public Optional<TreeNodeViewEventTarget<U>> getEventTarget(DomEvent<?> event) {
        NativeEvent nativeEvent = event.getNativeEvent();
        EventTarget eventTarget = nativeEvent.getEventTarget();
        if (Element.is(eventTarget)) {
            Element targetElement = Element.as(eventTarget);
            TreeNodeViewEventTarget.ViewTargetType handleIsTarget = TreeNodeViewEventTarget.ViewTargetType.CONTENT;
            while (targetElement != null) {
                if(isTreeNodeViewHandle(targetElement)) {
                    handleIsTarget = TreeNodeViewEventTarget.ViewTargetType.HANDLE;
                }
                java.util.Optional<TreeNodeView<U>> view = viewManager.getTreeNodeView(targetElement);
                if (view.isPresent()) {
                    return Optional.of(new TreeNodeViewEventTarget<U>(view.get(), handleIsTarget));
                }
                targetElement = targetElement.getParentElement();
            }
        }
        return Optional.absent();
    }

    private static boolean isTreeNodeViewHandle(Element targetElement) {
        String className = targetElement.getClassName();
        String handleStyleName = TreeNodeViewResources.RESOURCES.style().handle();
        return className.contains(handleStyleName);
    }



}
