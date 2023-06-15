package org.example.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import spark.ResponseTransformer;

@Component
@AllArgsConstructor
public class JsonResponseTransformer implements ResponseTransformer {

    private final ObjectMapper objectMapper;

    @Override
    public String render(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }
}
