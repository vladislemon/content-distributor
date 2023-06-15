package org.example.content;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.example.ContentDistributionService;
import org.example.ContentDistributionServiceAdditional;
import org.example.content.exception.BundleAlreadyPresentException;
import org.example.content.exception.BundleContentNotFoundException;
import org.example.content.exception.PublishListAlreadyExistsException;
import org.example.content.exception.PublishListNotExistsException;
import org.example.time.TimeService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
@AllArgsConstructor
public class SimpleInMemoryContentFacade implements ContentFacade {

    private final TimeService timeService;
    private final Map<ContentDistributionService.PublishListId, Instant> publishListMap = new HashMap<>();
    private final Map<BundleDataId, ContentDistributionServiceAdditional.UniqueBundleId> bundleIdMap = new HashMap<>();
    private final Map<ContentDistributionServiceAdditional.UniqueBundleId, byte[]> bundleDataMap = new HashMap<>();
    private final Map<ContentDistributionService.BundleId, NavigableMap<Instant, ContentDistributionServiceAdditional.UniqueBundleId>> uniqueBundleMap = new HashMap<>();

    @Override
    public ContentDistributionServiceAdditional.UniqueBundleId createBundle(
            ContentDistributionService.PublishListId publishListId,
            ContentDistributionService.BundleId bundleId,
            byte[] data
    ) {
        Instant publishDate = publishListMap.get(publishListId);
        if (publishDate == null) {
            throw new PublishListNotExistsException(publishListId);
        }
        BundleDataId bundleDataId = new BundleDataId(publishListId, bundleId);
        if (bundleIdMap.containsKey(bundleDataId)) {
            throw new BundleAlreadyPresentException(publishListId, bundleId);
        }
        ContentDistributionServiceAdditional.UniqueBundleId uniqueBundleId = new ContentDistributionServiceAdditional.UniqueBundleId(UUID.randomUUID().toString());
        bundleDataMap.put(uniqueBundleId, data);
        bundleIdMap.put(bundleDataId, uniqueBundleId);
        uniqueBundleMap.computeIfAbsent(bundleId, id -> new TreeMap<>()).put(publishDate, uniqueBundleId);
        return uniqueBundleId;
    }

    @Override
    public byte[] getBundleContent(ContentDistributionServiceAdditional.UniqueBundleId uniqueBundleId) {
        byte[] content = bundleDataMap.get(uniqueBundleId);
        if (content == null) {
            throw new BundleContentNotFoundException(uniqueBundleId);
        }
        return content;
    }

    @Override
    public ContentDistributionService.PublishListId createPublishList(String name, Instant publishDate) {
        ContentDistributionService.PublishListId publishListId = new ContentDistributionService.PublishListId(name);
        if (publishListMap.containsKey(publishListId)) {
            throw new PublishListAlreadyExistsException(publishListId);
        }
        publishListMap.put(publishListId, publishDate);
        return publishListId;
    }

    @Override
    public Map<ContentDistributionService.BundleId, ContentDistributionServiceAdditional.UniqueBundleId> findActualBundles(
            Set<ContentDistributionService.BundleId> bundleIds
    ) {
        Instant now = timeService.getNow();
        Map<ContentDistributionService.BundleId, ContentDistributionServiceAdditional.UniqueBundleId> result = new HashMap<>();
        for (ContentDistributionService.BundleId bundleId : bundleIds) {
            ContentDistributionServiceAdditional.UniqueBundleId uniqueBundleId = getUniqueBundleId(bundleId, now);
            if (uniqueBundleId != null) {
                result.put(bundleId, uniqueBundleId);
            }
        }
        return result;
    }

    private ContentDistributionServiceAdditional.UniqueBundleId getUniqueBundleId(
            ContentDistributionService.BundleId bundleId,
            Instant instant
    ) {
        NavigableMap<Instant, ContentDistributionServiceAdditional.UniqueBundleId> instantUniqueBundleIdNavigableMap = uniqueBundleMap.get(bundleId);
        if (instantUniqueBundleIdNavigableMap == null) {
            return null;
        }
        Map.Entry<Instant, ContentDistributionServiceAdditional.UniqueBundleId> instantUniqueBundleIdEntry = instantUniqueBundleIdNavigableMap.floorEntry(instant);
        if (instantUniqueBundleIdEntry == null) {
            return null;
        }
        return instantUniqueBundleIdEntry.getValue();
    }

    @Value
    private static class BundleDataId {
        ContentDistributionService.PublishListId publishListId;
        ContentDistributionService.BundleId bundleId;
    }
}
