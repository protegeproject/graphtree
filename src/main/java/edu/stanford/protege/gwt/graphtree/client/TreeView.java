package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasContextMenuHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/02/2014
 */
public interface TreeView<U extends Serializable> extends HasAllMouseHandlers, HasContextMenuHandlers, HasAllKeyHandlers, HasDoubleClickHandlers, RootNodeContainer {

    int getTreeNodeViewCount();

    TreeNodeView<U> getTreeNodeViewAt(int index);

    int getIndexOf(TreeNodeView<U> view);

}
