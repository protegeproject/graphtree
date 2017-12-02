package edu.stanford.protege.gwt.graphtree.shared;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/02/2014
 */
public class Path<N> implements Iterable<N>, Serializable {

    private static final Path<?> EMPTY_PATH = new Path();

    private static final transient Transform<?, ?> IDENTITY_TRANSFORM = element -> element;

    private List<N> path = new ArrayList<>();


    @SuppressWarnings("unchecked")
    public static <N> Transform<N, N> getIdentityTransform() {
        return (Transform<N, N>) IDENTITY_TRANSFORM;
    }

    /**
     * For serialization purposes only
     */
    private Path() {
    }

    public Path(List<N> path) {
        this.path = new ArrayList<>(path);
    }

    @SafeVarargs
    public static <N> Path<N> asPath(N... elements) {
        return new Path<>(Arrays.asList(elements));
    }

    @SuppressWarnings("unchecked")
    public static <N> Path<N> emptyPath() {
        return (Path<N>) EMPTY_PATH;
    }

    public List<N> asList() {
        return new ArrayList<>(path);
    }

    public int getLength() {
        return path.size();
    }

    public int size() {
        return path.size();
    }

    public N get(int index) {
        return path.get(index);
    }

    public boolean contains(N element) {
        return path.contains(element);
    }

    public <T> Path<T> transform(Transform<N, T> transform) {
        List<T> result = new ArrayList<>(path.size());
        for(N element : path) {
            result.add(transform.transform(element));
        }
        return new Path<>(result);
    }

    @Override
    @Nonnull
    public Iterator<N> iterator() {
        return path.iterator();
    }

    public Optional<N> getFirst() {
        if(path.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(path.get(0));
    }

    public Optional<N> getLast() {
        if(path.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(path.get(path.size() - 1));
    }

    public Optional<N> getLastPredecessor() {
        if(path.size() < 2) {
            return Optional.empty();
        }
        return Optional.of(path.get(path.size() - 2));
    }

    public Path<N> reverse() {
        return new Path<>(Lists.reverse(path));
    }

    @Override
    public int hashCode() {
        return "Path".hashCode() + path.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof Path)) {
            return false;
        }
        Path other = (Path) o;
        return this.path.equals(other.path);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Path")
                          .addValue(path)
                          .toString();
    }

    public interface Transform<N, T> {
        T transform(N element);
    }


}
