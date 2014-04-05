package edu.stanford.protege.gwt.graphtree.shared.graph.impl.servicebased;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public class ServiceBasedGraphModel {
//
//    private GraphModelServiceProvider service;
//
//    private GraphServiceFactory factory;
//
//
//    public ServiceBasedGraphModel(GraphModelServiceProvider service) {
//        this.service = service;
//    }
//
//    public void getKeyNodes(final GetKeyNodesCallback callback) {
//        GetKeyNodesAction action = factory.createGetKeyNodesAction();
//        service.execute(action, new AsyncCallback<GetKeyNodesResult>() {
//            public void onFailure(Throwable caught) {
//                // TODO!
//            }
//
//            public void onSuccess(GetKeyNodesResult result) {
//                callback.handleKeyNodes(result.getNodes());
//            }
//        });
//    }
//
//    public void getSuccessorNodes(Serializable userObject, final GetSuccessorNodesCallback callback) {
//        GetSuccessorNodesAction action = factory.createGetSuccessorNodesAction(userObject);
//        service.execute(action, new AsyncCallback<GetSuccessorNodesResult>() {
//            public void onFailure(Throwable caught) {
//                // TODO:
//            }
//
//            public void onSuccess(GetSuccessorNodesResult result) {
//                callback.handleSuccessorNodes(result.getNodes());
//            }
//        });
//    }
//
//    public void pollForEvents() {
//        service.getGraphModelEvents(new AsyncCallback<List<GraphModelChangedEvent>>() {
//            public void onFailure(Throwable caught) {
//                // TODO:
//            }
//
//            public void onSuccess(List<GraphModelChangedEvent> result) {
//                dispatchEvents(result);
//            }
//        });
//    }
//
//    private void dispatchEvents(List<GraphModelChangedEvent> graphModelEvents) {
//
//    }
//
//    public HandlerRegistration addGraphModelListener(final GraphModelListener listener) {
//        listeners.ADD(listener);
//        return new HandlerRegistration() {
//            public void removeHandler() {
//                listeners.remove(listener);
//            }
//        };
//    }
}
