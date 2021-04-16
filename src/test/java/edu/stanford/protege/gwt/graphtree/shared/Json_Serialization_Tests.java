package edu.stanford.protege.gwt.graphtree.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphModelChange;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphModelChangedEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-16
 */
public class Json_Serialization_Tests {

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new Jdk8Module());
        SimpleModule module = new SimpleModule();
        mapper.registerModule(module);
    }

    @Test
    public void shouldSerializePath() {
        roundTrip(new Path<>(Arrays.asList("A", "B", "C")), Path.class);
    }

    private <C> void roundTrip(C object, Class<? super  C> cls) {
        try {
            String s = mapper.writeValueAsString(object);
            System.out.println(s);
            Object read = mapper.readValue(s, cls);
            Assert.assertThat(object, is(read));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
