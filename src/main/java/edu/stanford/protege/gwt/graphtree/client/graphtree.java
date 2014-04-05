package edu.stanford.protege.gwt.graphtree.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class graphtree implements EntryPoint {

//    /**
//     * Create a remote service proxy to talk to the server-side Greeting service.
//     */
//    private final GraphModelServiceAsync service = GWT.create(GraphModelService.class);
//
//    private RemoteGraphModel graphModel;
//
////    private TreePresenter presenter;
//
//    private TreeNodeModel treeModel;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
//        graphModel = new RemoteGraphModel(service);
//        graphModel.startEventFetcher(10000);
//        treeModel = new GraphTreeNodeModel(graphModel);
//        MyDropHandler dropHandler = new MyDropHandler(graphModel);
//        TreeWidget treeView = new TreeWidget(treeModel, dropHandler);
//        final ScrollPanel w = new ScrollPanel(treeView);
//        w.setHeight("500px");
//        w.setWidth("500px");
//        RootPanel.get("slot").add(w);
    }

//    private class MyDropHandler implements TreeNodeDropHandler {
//
//        private RemoteGraphModel graphModel;
//
//        private MyDropHandler(RemoteGraphModel graphModel) {
//            this.graphModel = graphModel;
//        }
//
//        public void handleDrop(final TreeNodePath draggedNode, final TreeNodePath dropTarget, DropType dropType, final DropEndHandler endHandler) {
//            System.out.println("HANDLE DROP: " + draggedNode + " ON " + dropTarget);
//            OWLClass subCls = (OWLClass) draggedNode.getLast().getUserObject();
//            OWLClass fromSuperCls = (OWLClass) draggedNode.getLastParent().get().getUserObject();
//            OWLClass toSuperCls = (OWLClass) dropTarget.getLast().getUserObject();
//            graphModel.service.move(subCls, fromSuperCls, toSuperCls, new AsyncCallback<Void>() {
//                public void onFailure(Throwable caught) {
//                }
//
//                public void onSuccess(Void result) {
//                    System.out.println("Completed!");
//                    endHandler.handleDropComplete();
//                }
//            });
//        }
//
//        public boolean isDropPossible(TreeNodePath draggedNode, TreeNodePath dropTargetPath, DropType dropType) {
//            return !dropTargetPath.contains(draggedNode.getLast()) && (isClassOnClass(draggedNode.getLast(), dropTargetPath) || isNamedIndividualOnClass(draggedNode.getLast(), dropTargetPath));
//        }
//
//        private boolean isNamedIndividualOnClass(TreeNode draggedNode, TreeNodePath dropTargetPath) {
//            return (draggedNode.getUserObject() instanceof OWLNamedIndividual && dropTargetPath.getLast().getUserObject() instanceof OWLClass);
//        }
//
//        private boolean isClassOnClass(TreeNode draggedNode, TreeNodePath dropTargetPath) {
//            return (draggedNode.getUserObject() instanceof OWLClass && dropTargetPath.getLast().getUserObject() instanceof OWLClass);
//        }
//    }
//
//    private static class RemoteGraphModel implements GraphModel {
//
//        private final HandlerManager handlerManager;
//
//        private GraphModelServiceAsync service;
//
//        private Timer eventTimer;
//
//        private RemoteGraphModel(GraphModelServiceAsync s) {
//            this.service = s;
//            handlerManager = new HandlerManager(this);
//            eventTimer = new Timer() {
//                @Override
//                public void run() {
//                    service.getEvents(new AsyncCallback<List<GraphModelChange>>() {
//                        public void onFailure(Throwable caught) {
//                        }
//
//                        public void onSuccess(List<GraphModelChange> result) {
//                            fireEvent(new GraphModelChangedEvent(result));
//                        }
//                    });
//                }
//            };
//        }
//
//        public void startEventFetcher(int delay) {
//            eventTimer.scheduleRepeating(delay);
//        }
//
//        public void stopEventFetcher() {
//            eventTimer.cancel();
//        }
//
//        public void getKeyNodes(final GetKeyNodesCallback callback) {
//            service.getRootNodes(new AsyncCallback<List<GraphNode>>() {
//                public void onFailure(Throwable caught) {
//                    Window.alert(caught.getMessage());
//                }
//
//                public void onSuccess(List<GraphNode> result) {
//                    callback.handleKeyNodes(result);
//                }
//            });
//        }
//
//        public void getSuccessorNodes(Serializable userObject, final GetSuccessorNodesCallback callback) {
//            service.getChildren(userObject, new AsyncCallback<List<GraphNode>>() {
//                public void onFailure(Throwable caught) {
//                    Window.alert(caught.getMessage());
//                }
//
//                public void onSuccess(List<GraphNode> result) {
//                    Collections.sort(result);
//                    callback.handleSuccessorNodes(result);
//                }
//            });
//        }
//
//        public HandlerRegistration addGraphModelHandler(GraphModelChangedHandler handler) {
//            return handlerManager.addHandler(GraphModelChangedEvent.getType(), handler);
//        }
//
//        public void fireEvent(GwtEvent<?> event) {
//            handlerManager.fireEvent(event);
//        }
//    }
}
