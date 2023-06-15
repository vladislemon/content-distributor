package org.example.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.ContentDistributionService;

import java.io.IOException;

public class PublishListIdDeserializer extends StdDeserializer<ContentDistributionService.PublishListId> {

    public PublishListIdDeserializer() {
        this(null);
    }

    public PublishListIdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ContentDistributionService.PublishListId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return new ContentDistributionService.PublishListId(node.get("id").asText());
    }
}
