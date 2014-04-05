package edu.stanford.protege.gwt.graphtree.client;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.*;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNodeId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public class TreeNodeViewImpl<U extends Serializable> extends Composite implements TreeNodeView<U>{

    static {
        TreeNodeViewResources.RESOURCES.style().ensureInjected();
    }

    public static final int INDENT_EM = 2;

    private static final CellTree.Resources RESOURCES = GWT.create(CellTree.Resources.class);

    private static TreeNodeViewImplUiBinder ourUiBinder = GWT.create(TreeNodeViewImplUiBinder.class);

    @UiField
    protected Widget handle;

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

    private TreeNode<U> node;

    private boolean leaf = true;

    private int depth;

    private boolean loadingIndicatorDisplayed = false;

    private TreeNodeView<U> previousSibling;

    private TreeNodeView<U> nextSibling;

    public TreeNodeViewImpl(TreeNode<U> treeNode) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        updateHandleImage();
        getElement().setDraggable(Element.DRAGGABLE_TRUE);
        this.node = treeNode;
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

    public TreeNodeId getNodeId() {
        return node.getId();
    }

    @Override
    public U getUserObject() {
        return node.getUserObject();
    }

    @Override
    public TreeNode<U> getNode() {
        return node;
    }

    public void setNode(TreeNode<U> node) {
        this.node = checkNotNull(node);
    }

//    @Override
//    public String getShortForm() {
//        return shortForm;
//    }

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
        animation.run(200);
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
        return Optional.absent();
    }

    public Optional<TreeNodeView<U>> getPreviousSibling() {
        return Optional.fromNullable(previousSibling);
//        return new TreeNodeViewPreviousSiblingFinder(this).getPreviousSibling();
    }

    public Optional<TreeNodeView<U>> getNextSibling() {
        return Optional.fromNullable(nextSibling);
//        return new TreeNodeViewNextSiblingFinder(this).getNextSibling();
    }

    public Optional<TreeNodeView<U>> getFirstChildView() {
        if(childContainer == null) {
            return Optional.absent();
        }
        if (childContainer.getWidgetCount() == 0) {
            return Optional.absent();
        }
        return Optional.of((TreeNodeView<U>) childContainer.getWidget(0));
    }

    public Optional<TreeNodeView<U>> getLastChildView() {
        if(childContainer == null) {
            return Optional.absent();
        }
        int widgetCount = childContainer.getWidgetCount();
        if (widgetCount == 0) {
            return Optional.absent();
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
        final ImageResource handleImageResource;
        if(loadingIndicatorDisplayed || dataState == DataState.LOADING) {
            handleImageResource = RESOURCES.cellTreeLoading();
        }
        else {

            if(viewState == TreeNodeViewState.COLLAPSED) {
                handleImageResource = RESOURCES.cellTreeClosedItem();
            }
            else {
                handleImageResource = RESOURCES.cellTreeOpenItem();
            }
        }
        handleImage.setResource(handleImageResource);
        handle.getElement().getStyle().setVisibility(shouldShowHandle() ? Style.Visibility.VISIBLE : Style.Visibility.HIDDEN);
    }

    private boolean shouldShowHandle() {
        return !leaf || dataState == DataState.LOADING || loadingIndicatorDisplayed;
    }

    private int getAnimationDuration() {
        int childCount = getExpandedDescendantCount();
        int time = childCount * 100;
        if (time < 200) {
            return 200;
        }
        else if (time > 500) {
            return 500;
        }
        else {
            return time;
        }
    }

    interface TreeNodeViewImplUiBinder extends UiBinder<HTMLPanel, TreeNodeViewImpl> {

    }

    private static class AnimateOpen extends Animation {

        private int finalHeight;

        private Widget holderWidget;

        private Widget childWidget;

        private int initialHeight;

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

        private int rowCount;

        private Widget holderWidget;

        private Widget childWidget;

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

        private Widget widget;

        private int initialHeight;

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