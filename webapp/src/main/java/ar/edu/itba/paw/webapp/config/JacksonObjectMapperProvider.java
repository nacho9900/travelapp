package ar.edu.itba.paw.webapp.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Provider
@Component
public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper>
{
    private final ObjectMapper defaultObjectMapper;

    @Autowired
    public JacksonObjectMapperProvider(ObjectMapper objectMapper){
        defaultObjectMapper = objectMapper;
    }

    @Override
    public ObjectMapper getContext( Class<?> type ) {
        return defaultObjectMapper;
    }
}
