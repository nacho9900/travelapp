package ar.edu.itba.paw.webapp.dto.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Collection;

public class CollectionSerializer<T> extends JsonSerializer<Collection<T>>
{
    @Override
    public void serialize( Collection<T> ts, JsonGenerator jsonGenerator, SerializerProvider serializerProvider ) throws IOException {
        jsonGenerator.writeStartArray();
        for ( T element : ts ) {
            jsonGenerator.writeObject( element );
        }
        jsonGenerator.writeEndArray();
    }
}
