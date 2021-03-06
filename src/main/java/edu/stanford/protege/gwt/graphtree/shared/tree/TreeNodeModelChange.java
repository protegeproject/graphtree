package edu.stanford.protege.gwt.graphtree.shared.tree;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/01/2014
 */
public abstract class TreeNodeModelChange<U extends Serializable> implements Serializable, IsSerializable {

    public abstract void accept(TreeNodeModelChangeVisitor visitor);

//    public abstract Set<TreeNode<U>> getTreeNodes();
}
