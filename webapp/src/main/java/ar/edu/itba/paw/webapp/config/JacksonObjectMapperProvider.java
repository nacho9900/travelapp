package ar.edu.itba.paw.webapp.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Provider
public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper>
{
    final ObjectMapper defaultObjectMapper;

    public JacksonObjectMapperProvider(){
        defaultObjectMapper = createDefaultMapper();
    }

    @Override
    public ObjectMapper getContext( Class<?> type ) {
        return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy hh:mm" );
        dateFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );

        objectMapper = objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL )
                                   .setDefaultPropertyInclusion( JsonInclude.Include.NON_NULL )
                                   .setPropertyNamingStrategy( PropertyNamingStrategy.LOWER_CAMEL_CASE )
                                   .registerModule( new JavaTimeModule() )
                                   .registerModule( new Jdk8Module() )
                                   .disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS )
                                   .setDateFormat( dateFormat );

        return objectMapper;
    }
}
