package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.content.exception.BundleAlreadyPresentException;
import org.example.content.exception.BundleContentNotFoundException;
import org.example.content.exception.PublishListAlreadyExistsException;
import org.example.content.exception.PublishListNotExistsException;
import org.example.json.JsonResponseTransformer;
import org.example.url.BundleUrlResolver;
import org.springframework.stereotype.Controller;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Controller
@AllArgsConstructor
public class ContentDistributionController {

    private static final TypeReference<Set<ContentDistributionService.BundleId>> BUNDLE_ID_SET_TR = new TypeReference<Set<ContentDistributionService.BundleId>>() {
    };
    public static final TypeReference<ContentDistributionService.PublishListId> PUBLISH_LIST_ID_TR = new TypeReference<ContentDistributionService.PublishListId>() {
    };
    public static final TypeReference<ContentDistributionService.BundleId> BUNDLE_ID_TP = new TypeReference<ContentDistributionService.BundleId>() {
    };
    private final ObjectMapper objectMapper;
    private final JsonResponseTransformer jsonResponseTransformer;
    private final ContentDistributionService contentDistributionService;
    private final ContentDistributionServiceAdditional contentDistributionServiceAdditional;
    private final BundleUrlResolver bundleUrlResolver;

    @PostConstruct
    public void setupEndpoints() {
        Spark.post("/bundle/urls", "application/json", this::publicGetBundleUrls, jsonResponseTransformer);
        Spark.post("/publish-list", this::innerCreatePublishList, jsonResponseTransformer);
        Spark.post("/bundle/upload", this::innerUploadContent);
        Spark.post("/time/now", this::adminSetNow);
        Spark.post(bundleUrlResolver.getBundleUrlPattern(), this::publicGetBundleData);

        Spark.exception(BundleAlreadyPresentException.class, (e, request, response) -> {
            response.status(400);
            response.body(e.getMessage());
        });
        Spark.exception(PublishListAlreadyExistsException.class, (e, request, response) -> {
            response.status(400);
            response.body(e.getMessage());
        });
        Spark.exception(PublishListNotExistsException.class, (e, request, response) -> {
            response.status(400);
            response.body(e.getMessage());
        });
        Spark.exception(BundleContentNotFoundException.class, (e, request, response) -> {
            response.status(404);
            response.body(e.getMessage());
        });
        Spark.exception(JsonProcessingException.class, (e, request, response) -> {
            response.status(400);
            response.body(e.getMessage());
        });
    }

    public Object publicGetBundleUrls(Request request, Response response) throws IOException {
        response.type("application/json");
        Set<ContentDistributionService.BundleId> bundleIds = objectMapper.readValue(request.bodyAsBytes(), BUNDLE_ID_SET_TR);
        return contentDistributionService.publicGetBundleUrls(bundleIds);
    }

    public Object innerCreatePublishList(Request request, Response response) {
        response.type("application/json");
        String name = request.queryParams("name");
        Instant publishDate = Instant.parse(request.queryParams("publishDate"));
        return contentDistributionService.innerCreatePublishList(name, publishDate);
    }

    public Object innerUploadContent(Request request, Response response) throws JsonProcessingException {
        ContentDistributionService.PublishListId publishListId = objectMapper.readValue(request.queryParams("publishListId"), PUBLISH_LIST_ID_TR);
        ContentDistributionService.BundleId bundleId = objectMapper.readValue(request.queryParams("bundleId"), BUNDLE_ID_TP);
        return contentDistributionService.innerUploadContent(publishListId, bundleId, request.bodyAsBytes());
    }

    public Object adminSetNow(Request request, Response response) {
        String newNowParam = request.queryParams("newNow");
        Instant newNow = newNowParam == null || newNowParam.isEmpty() ? null : Instant.parse(newNowParam);
        contentDistributionService.adminSetNow(Optional.ofNullable(newNow));
        response.status(201);
        return "";
    }

    public Object publicGetBundleData(Request request, Response response) {
        String uniqueBundleIdParam = request.params(bundleUrlResolver.getBundleUrlIdPlaceholder());
        ContentDistributionServiceAdditional.UniqueBundleId uniqueBundleId = new ContentDistributionServiceAdditional.UniqueBundleId(uniqueBundleIdParam);
        return contentDistributionServiceAdditional.publicGetBundleData(uniqueBundleId);
    }

}
