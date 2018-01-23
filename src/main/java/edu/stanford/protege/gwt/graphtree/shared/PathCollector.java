package edu.stanford.protege.gwt.graphtree.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 19 Dec 2017
 */
public class PathCollector<N extends Serializable> implements Collector<N, List<N>, Path<N>> {

    @Override
    public Supplier<List<N>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<N>, N> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<N>> combiner() {
        return (firstList, secondList) -> {
            firstList.addAll(secondList);
            return firstList;
        };
    }

    @Override
    public Function<List<N>, Path<N>> finisher() {
        return Path::new;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

    public static <N extends Serializable> PathCollector<N> toPath() {
        return new PathCollector<>();
    }
}
