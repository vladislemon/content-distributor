package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.json.BundleIdDeserializer;
import org.example.json.PublishListIdDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ContentDistributor {

    public static void main(String[] args) {
        SpringApplication.run(ContentDistributor.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ContentDistributionService.PublishListId.class, new PublishListIdDeserializer());
        module.addDeserializer(ContentDistributionService.BundleId.class, new BundleIdDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

}
