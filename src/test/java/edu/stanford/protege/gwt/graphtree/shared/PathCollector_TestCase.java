package edu.stanford.protege.gwt.graphtree.shared;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.stream.Stream;

import static edu.stanford.protege.gwt.graphtree.shared.PathCollector.toPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 19 Dec 2017
 */
@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class PathCollector_TestCase<N extends Serializable> {

    @Mock
    N first, second, third;

    @Test
    public void shouldCollectToPath() {
        Path<N> path = Stream.of(first, second, third)
                             .collect(toPath());
        assertThat(path.asList(), hasItems(first, second, third));
    }
}
