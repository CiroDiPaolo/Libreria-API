package com.LibreriaApi.Model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class UserIdSerializer extends StdSerializer<UserEntity> {

    public UserIdSerializer() {
        super(UserEntity.class);
    }

    @Override
    public void serialize(UserEntity user, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (user == null) {
            gen.writeNull();
        } else {
            gen.writeNumber(user.getId());
        }
    }
}