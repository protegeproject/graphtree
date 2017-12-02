package edu.stanford.protege.gwt.graphtree.shared.graph;


import edu.stanford.protege.gwt.graphtree.shared.Path;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/01/2014
 */
public class PathFinder<N> {


    private final HasSuccessors<N> hasSuccessors;

    public PathFinder(HasSuccessors<N> hasSuccessors) {
        this.hasSuccessors = hasSuccessors;
    }

    public Collection<Path<N>> getPaths(N from, N to) {
        HashSet<Path<N>> result = new HashSet<Path<N>>();
        ArrayList<N> currentPath = new ArrayList<N>();
        currentPath.add(from);
        doIt(from, to, from, currentPath, new HashSet<N>(), result);
        return result;
    }

    public void doIt(N from, N to, N current, List<N> currentPath, Set<N> visited, Set<Path<N>> paths) {
        if(current.equals(to)) {
            // Got a path
            paths.add(new Path<N>(currentPath));
            return;
        }
        for(N successor : hasSuccessors.getSuccessors(current)) {
            if (!visited.contains(successor)) {
                currentPath.add(successor);
                visited.add(successor);
                doIt(from, to, successor, currentPath, visited, paths);
                currentPath.remove(successor);
                visited.remove(successor);
            }
        }
    }
}
