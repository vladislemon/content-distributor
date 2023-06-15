package org.example;

import lombok.AllArgsConstructor;
import org.example.content.ContentFacade;
import org.example.time.TimeService;
import org.example.url.BundleUrlResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContentDistributionServiceImpl implements ContentDistributionService, ContentDistributionServiceAdditional {

    private final TimeService timeService;
    private final ContentFacade contentFacade;
    private final BundleUrlResolver bundleUrlResolver;

    @Override
    public BundleUrlsDto publicGetBundleUrls(Set<BundleId> bundleIds) {
        if (CollectionUtils.isEmpty(bundleIds)) {
            return new BundleUrlsDto(Collections.emptyMap());
        }
        Map<BundleId, UniqueBundleId> actualBundles = contentFacade.findActualBundles(bundleIds);
        Map<BundleId, String> bundleUrlsMap = actualBundles
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> bundleUrlResolver.getBundleUrl(entry.getValue())
                ));
        return new BundleUrlsDto(bundleUrlsMap);
    }

    @Override
    public PublishListId innerCreatePublishList(String name, Instant publishDate) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(publishDate);
        return contentFacade.createPublishList(name, publishDate);
    }

    @Override
    public boolean innerUploadContent(PublishListId publishListId, BundleId bundleId, byte[] bundleContents) {
        Objects.requireNonNull(publishListId);
        Objects.requireNonNull(bundleId);
        Objects.requireNonNull(bundleContents);
        try {
            contentFacade.createBundle(publishListId, bundleId, bundleContents);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void adminSetNow(Optional<Instant> newNow) {
        Objects.requireNonNull(newNow);
        timeService.setNow(newNow.orElse(null));
    }

    @Override
    public byte[] publicGetBundleData(UniqueBundleId uniqueBundleId) {
        Objects.requireNonNull(uniqueBundleId);
        return contentFacade.getBundleContent(uniqueBundleId);
    }
}
