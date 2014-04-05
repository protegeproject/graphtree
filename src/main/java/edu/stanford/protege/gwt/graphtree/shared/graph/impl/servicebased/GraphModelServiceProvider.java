package edu.stanford.protege.gwt.graphtree.shared.graph.impl.servicebased;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphModelChangedEvent;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public interface GraphModelServiceProvider {

    <A extends GraphServiceAction<R>, R extends GraphServiceResult> void execute(A action, AsyncCallback<R> callback);

    void getGraphModelEvents(AsyncCallback<List<GraphModelChangedEvent>> callback);

}
