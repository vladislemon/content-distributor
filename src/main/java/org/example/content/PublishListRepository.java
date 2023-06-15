package org.example.content;

import org.example.ContentDistributionService;
import org.example.ContentDistributionServiceAdditional;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

public interface PublishListRepository {

    @NonNull
    ContentDistributionService.PublishListId createPublishList(
            @NonNull String name,
            @NonNull Instant publishDate);

    @NonNull
    Map<ContentDistributionService.BundleId, ContentDistributionServiceAdditional.UniqueBundleId> findActualBundles(
            @NonNull Set<ContentDistributionService.BundleId> bundleIds);

}
