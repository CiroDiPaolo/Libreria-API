package com.LibreriaApi.Model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Data;

import java.io.IOException;

public class MultimediaIdSerializer extends StdSerializer<Multimedia> {

    public MultimediaIdSerializer() {
        super(Multimedia.class);
    }
    @Override
    public void serialize(Multimedia multi, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", multi.getId());
        gen.writeStringField("category", multi.getCategory().toString());
        gen.writeEndObject();
    }
}
