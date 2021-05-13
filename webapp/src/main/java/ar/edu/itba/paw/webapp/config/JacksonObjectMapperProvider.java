package ar.edu.itba.paw.webapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

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
