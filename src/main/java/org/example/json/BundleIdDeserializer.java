package org.example.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.ContentDistributionService;

import java.io.IOException;

public class BundleIdDeserializer extends StdDeserializer<ContentDistributionService.BundleId> {

    public BundleIdDeserializer() {
        this(null);
    }

    public BundleIdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ContentDistributionService.BundleId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return new ContentDistributionService.BundleId(node.get("field1").asText(), node.get("field2").asText());
    }
}
