package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.collect.Lists;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.*;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class TreeNodeViewImpl<U extends Serializable> extends Composite implements TreeNodeView<U>{

    private static final int MINIMUM_ANIMATION_TIME = 200;
    private static final int MAXIMUM_ANIMATION_TIME = 500;

    static {
        TreeNodeViewResources.RESOURCES.style().ensureInjected();
    }

    public static final int INDENT_EM = 2;

    private static final TreeNodeViewResources RESOURCES = GWT.create(TreeNodeViewResources.class);

    private static final TreeNodeViewImplUiBinder ourUiBinder = GWT.create(TreeNodeViewImplUiBinder.class);

    @UiField
    protected Image handleImage;

    @UiField
    protected HTML rendering;

    @UiField
    protected Widget content;

    @UiField
    protected FlowPanel childContainer;

    @UiField
    protected LazyPanel childContainerHolder;

    private boolean selected = false;

    private DataState dataState = DataState.UNLOADED;

    private TreeNodeViewState viewState = TreeNodeViewState.COLLAPSED;

    private boolean pruned = false;

    private boolean leaf = true;

    private int depth;

    private boolean loadingIndicatorDisplayed = false;

    private TreeNodeView<U> previousSibling;

    private TreeNodeView<U> nextSibling;

    private TreeNodeId nodeId;

    private U userObject;

    private boolean loaded = false;

    public TreeNodeViewImpl(@Nonnull TreeNodeId nodeId, U userObject) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        updateHandleImage();
        getElement().setDraggable(Element.DRAGGABLE_TRUE);
        this.nodeId = checkNotNull(nodeId);
        this.userObject = checkNotNull(userObject);
    }

    public DataState getDataState() {
        return dataState;
    }

    public void setDataState(DataState state) {
        if(state != dataState) {
            dataState = state;
            updateHandleImage();
        }
    }

    public void setLoadingIndicatorDisplayed(boolean displayed) {
        this.loadingIndicatorDisplayed = displayed;
        updateHandleImage();
    }

    @Nonnull
    public TreeNodeId getNodeId() {
        return nodeId;
    }

    @Nonnull
    @Override
    public U getUserObject() {
        return userObject;
    }

    public void setUserObject(@Nonnull U userObject) {
        this.userObject = checkNotNull(userObject);
    }

    @Nonnull
    @Override
    public TreeNode<U> getNode() {
        return new TreeNode<>(nodeId, userObject);
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
        content.getElement().getStyle().setPaddingLeft(this.depth * INDENT_EM, Style.Unit.EM);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean sel) {
        selected = sel;
        if (sel) {
            content.addStyleName(TreeNodeViewResources.RESOURCES.style().rowSelected());
        }
        else {
            content.removeStyleName(TreeNodeViewResources.RESOURCES.style().rowSelected());
        }
    }

    public void setRendering(String rendering) {
        this.rendering.setHTML(rendering);
    }

    public boolean isExpanded() {
        return viewState == TreeNodeViewState.EXPANDED;
    }

    public boolean isCollapsed() {
        return viewState == TreeNodeViewState.COLLAPSED;
    }

    public void setExpanded() {
        setViewState(TreeNodeViewState.EXPANDED);
    }

    public void setCollapsed() {
        setViewState(TreeNodeViewState.COLLAPSED);
    }

    public TreeNodeViewState getViewState() {
        return viewState;
    }

    /**
     * Sets the expansion state of this {@link TreeNodeView}.  If the state is set to {@link TreeNodeViewState#EXPANDED}
     * then the children of this view will be shown.  If the state is set to {@link TreeNodeViewState#COLLAPSED} then
     * the children of this view will be hidden.
     * @param state the state.
     */
    public void setViewState(TreeNodeViewState state) {
        if (viewState == state) {
            return;
        }
        viewState = state;
        updateHandleImage();
        childContainerHolder.ensureWidget();
        childContainer.setVisible(true);
        if (state == TreeNodeViewState.EXPANDED) {
            animateOpen();
        }
        else if(state == TreeNodeViewState.COLLAPSED) {
            animateClosed();
        }
    }

    @Override
    public void setPruned(boolean pruned) {
        if(this.pruned == pruned) {
            return;
        }
        this.pruned = pruned;
        if(pruned) {
            addStyleName(RESOURCES.style().pruned());
        }
        else {
            removeStyleName(RESOURCES.style().pruned());
        }
        updateHandleImage();
    }

    public boolean isEmpty() {
        return childContainer == null || childContainer.getWidgetCount() == 0;
    }

    public void addChildView(TreeNodeView child) {
        // Avoid duplicates?
        childContainerHolder.ensureWidget();
        childContainer.add(child);
        TreeNodeViewImpl previousSibling = null;
        if(childContainer.getWidgetCount() != 0) {
            previousSibling = (TreeNodeViewImpl) childContainer.getWidget(0);
            previousSibling.nextSibling = child;
        }
        ((TreeNodeViewImpl) child).previousSibling = previousSibling;
        ((TreeNodeViewImpl) child).nextSibling = null;

    }




    public void removeChildView(TreeNodeView child) {
        if(childContainer == null) {
            return;
        }
        int childIndex = childContainer.getWidgetIndex(child);
        if(childIndex == -1) {
            return;
        }
        TreeNodeViewImpl previousSibling = null;
        if(childIndex > 0) {
            int previousChildIndex = childIndex - 1;
            previousSibling = ((TreeNodeViewImpl) childContainer.getWidget(previousChildIndex));
        }
        TreeNodeViewImpl nextSibling = null;
        if(childIndex + 1 < childContainer.getWidgetCount()) {
            int nextChildIndex = childIndex + 1;
            nextSibling = ((TreeNodeViewImpl) childContainer.getWidget(nextChildIndex));
        }
//        childContainer.remove(child);
        AnimateRemove animation = new AnimateRemove(child.asWidget());
        animation.run(MINIMUM_ANIMATION_TIME);
        if(previousSibling != null) {
            previousSibling.nextSibling = nextSibling;
        }
        if(nextSibling != null) {
            nextSibling.previousSibling = previousSibling;
        }
    }

    public Iterable<TreeNodeView<U>> getChildViews() {
        if(childContainer == null) {
            return Collections.emptyList();
        }
        List<TreeNodeView<U>> result = Lists.newArrayList();
        for (Widget childWidget : childContainer) {
            if (childWidget instanceof TreeNodeView) {
                result.add((TreeNodeView<U>) childWidget);
            }
        }
        return result;
    }

    public void setChildViews(List<TreeNodeView<U>> childViews) {
        childContainerHolder.ensureWidget();
        Element childElement = childContainer.getElement();
        Element parentElement = childContainer.getParent().getElement();
        Node prevSib = childElement.getPreviousSibling();
        childElement.removeFromParent();
        childContainer.clear();
        for (TreeNodeView view : childViews) {
            childContainer.add(view);
        }
        if (prevSib != null) {
            parentElement.insertAfter(childElement, prevSib);
        }
        else {
            parentElement.insertFirst(childElement);
        }
        TreeNodeViewImpl previousSibling = null;
        int widgetCount = childContainer.getWidgetCount();
        for(int i = 0; i < widgetCount; i++) {
            Widget widget = childContainer.getWidget(i);
            TreeNodeViewImpl currentChild = (TreeNodeViewImpl) widget;
            currentChild.previousSibling = previousSibling;
            if(previousSibling != null) {
                previousSibling.nextSibling = currentChild;
            }
            if(i == widgetCount - 1) {
                currentChild.nextSibling = null;
            }
            previousSibling = currentChild;
        }

    }



    public Optional<TreeNodeView<U>> getParentView() {
        Widget parent = getParent();
        while (parent != null) {
            if (parent instanceof TreeNodeView) {
                return Optional.of((TreeNodeView<U>) parent);
            }
            parent = parent.getParent();
        }
        return Optional.empty();
    }

    public Optional<TreeNodeView<U>> getPreviousSibling() {
        return Optional.ofNullable(previousSibling);
//        return new TreeNodeViewPreviousSiblingFinder(this).getPreviousSibling();
    }

    public Optional<TreeNodeView<U>> getNextSibling() {
        return Optional.ofNullable(nextSibling);
//        return new TreeNodeViewNextSiblingFinder(this).getNextSibling();
    }

    public Optional<TreeNodeView<U>> getFirstChildView() {
        if(childContainer == null) {
            return Optional.empty();
        }
        if (childContainer.getWidgetCount() == 0) {
            return Optional.empty();
        }
        return Optional.of((TreeNodeView<U>) childContainer.getWidget(0));
    }

    public Optional<TreeNodeView<U>> getLastChildView() {
        if(childContainer == null) {
            return Optional.empty();
        }
        int widgetCount = childContainer.getWidgetCount();
        if (widgetCount == 0) {
            return Optional.empty();
        }
        childContainer.getWidgetIndex(this);
        return Optional.of((TreeNodeView<U>) childContainer.getWidget(widgetCount - 1));
    }

    public void setLeaf(boolean isLeaf) {
        this.leaf = isLeaf;
        updateHandleImage();
    }

    public void scrollIntoView() {
        DOM.scrollIntoView(content.getElement());
    }

    @Override
    public void setHidden(boolean hidden) {
        if(hidden) {
            setHeight("0px");
        }
        else {
            setHeight("auto");
        }
    }

    public void setDragOver(boolean b) {
        String style = TreeNodeViewResources.RESOURCES.style().rowDragOver();
        if (b) {
            content.addStyleName(style);
        }
        else {
            content.removeStyleName(style);
        }
    }

    public IsWidget getDragWidget() {
        return rendering;
    }

    private void animateClosed() {
        childContainerHolder.ensureWidget();
        AnimateClose close = new AnimateClose(childContainer.getOffsetHeight(), childContainerHolder, childContainer);
        close.run(getAnimationDuration());
    }

    private void animateOpen() {
        childContainerHolder.ensureWidget();
        AnimateOpen ani = new AnimateOpen(childContainer.getOffsetHeight(), childContainerHolder, childContainer);
        ani.run(getAnimationDuration());
    }

    private int getExpandedDescendantCount() {
        if(childContainer == null) {
            return 0;
        }
        int count = childContainer.getWidgetCount();
        for (Widget childWidget : childContainer) {
            count += ((TreeNodeViewImpl) childWidget).getExpandedDescendantCountRecursive();
        }
        return count;
    }

    private int getExpandedDescendantCountRecursive() {
        if (viewState == TreeNodeViewState.COLLAPSED) {
            return 0;
        }
        int count = childContainer.getWidgetCount();
        for (Widget childWidget : childContainer) {
            count += ((TreeNodeViewImpl) childWidget).getExpandedDescendantCount();
        }
        return count;
    }

    private void updateHandleImage() {
        final String handleImageResourceUri;
        if(loadingIndicatorDisplayed || dataState == DataState.LOADING) {
            handleImageResourceUri = RESOURCES.loading().getSafeUri().asString();
        }
        else {
            if(pruned) {
                if(viewState == TreeNodeViewState.COLLAPSED) {
                    handleImageResourceUri = RESOURCES.prunedCollapsed().getSafeUri().asString();
                }
                else {
                    handleImageResourceUri = RESOURCES.prunedExpanded().getSafeUri().asString();
                }
            }
            else {
                if(viewState == TreeNodeViewState.COLLAPSED) {
                    handleImageResourceUri = RESOURCES.collapsed().getSafeUri().asString();
                }
                else {
                    handleImageResourceUri = RESOURCES.expanded().getSafeUri().asString();
                }
            }
        }
        handleImage.setUrl(handleImageResourceUri);
        handleImage.getElement().getStyle().setVisibility(shouldShowHandle() ? Style.Visibility.VISIBLE : Style.Visibility.HIDDEN);
    }

    private boolean shouldShowHandle() {
        return !leaf || dataState == DataState.LOADING || loadingIndicatorDisplayed;
    }

    private int getAnimationDuration() {
        int childCount = getExpandedDescendantCount();
        int time = childCount * 100;
        if (time < MINIMUM_ANIMATION_TIME) {
            return MINIMUM_ANIMATION_TIME;
        }
        else if (time > MAXIMUM_ANIMATION_TIME) {
            return MAXIMUM_ANIMATION_TIME;
        }
        else {
            return time;
        }
    }

    interface TreeNodeViewImplUiBinder extends UiBinder<HTMLPanel, TreeNodeViewImpl> {

    }

    private static class AnimateOpen extends Animation {

        private final int finalHeight;

        private final Widget holderWidget;

        private final Widget childWidget;

        private final int initialHeight;

        private AnimateOpen(int finalHeight, Widget holderWidget, Widget childWidget) {
            this.finalHeight = finalHeight;
            this.holderWidget = holderWidget;
            this.childWidget = childWidget;
            initialHeight = holderWidget.getOffsetHeight();
        }

        @Override
        protected void onStart() {
            holderWidget.setVisible(true);
        }

        @Override
        protected void onUpdate(double progress) {
            int totalHeight = (finalHeight);
            double intermediateHeight = (totalHeight * progress);
            if (intermediateHeight < initialHeight) {
                intermediateHeight = initialHeight;
            }
            if (intermediateHeight > finalHeight) {
                intermediateHeight = finalHeight;
            }
            holderWidget.setHeight(intermediateHeight + "px");
            childWidget.getElement().getStyle().setTop(-(totalHeight - intermediateHeight), Style.Unit.PX);
        }

        @Override
        protected void onComplete() {
            holderWidget.setHeight("auto");
            childWidget.getElement().getStyle().setTop(0, Style.Unit.PX);
        }
    }

    private static class AnimateClose extends Animation {

        private final int rowCount;

        private final Widget holderWidget;

        private final Widget childWidget;

        private AnimateClose(int rowCount, Widget holderWidget, Widget childWidget) {
            this.rowCount = rowCount;
            this.holderWidget = holderWidget;
            this.childWidget = childWidget;
        }

        @Override
        protected void onUpdate(double progress) {
            int totalHeight = (rowCount);
            double intermediateHeight = (totalHeight * (1 - progress));
            holderWidget.getElement().getStyle().setHeight(intermediateHeight, Style.Unit.PX);
            childWidget.getElement().getStyle().setTop(-(totalHeight - intermediateHeight), Style.Unit.PX);
        }

        @Override
        protected void onComplete() {
            childWidget.setVisible(false);
        }
    }


    private static class AnimateRemove extends Animation {

        private final Widget widget;

        private final int initialHeight;

        private AnimateRemove(Widget widget) {
            this.widget = widget;
            initialHeight = widget.getOffsetHeight();
        }

        @Override
        protected void onStart() {
            widget.getElement().getStyle().setOpacity(0);
        }

        @Override
        protected void onUpdate(double progress) {
            int height = (int)(initialHeight * (1.0 - progress));
            widget.getElement().getStyle().setHeight(height, Style.Unit.PX);
        }

        @Override
        protected void onComplete() {
            widget.removeFromParent();
            widget.setHeight("auto");
            widget.getElement().getStyle().setOpacity(1.0);
        }
    }

}