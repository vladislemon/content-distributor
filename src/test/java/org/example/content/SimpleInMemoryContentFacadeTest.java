package org.example.content;

import org.example.ContentDistributionService;
import org.example.ContentDistributionServiceAdditional;
import org.example.content.exception.BundleAlreadyPresentException;
import org.example.content.exception.PublishListAlreadyExistsException;
import org.example.time.ConstantTimeService;
import org.example.time.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SimpleInMemoryContentFacadeTest {

    TimeService timeService = new ConstantTimeService();
    SimpleInMemoryContentFacade contentFacade;

    @BeforeEach
    void setup() {
        contentFacade = new SimpleInMemoryContentFacade(timeService);
    }

    @Test
    void createPublishListsWithDifferentNames_ok() {
        contentFacade.createPublishList("PL-1", Instant.now());
        contentFacade.createPublishList("PL-2", Instant.now());
    }

    @Test
    void createPublishListsWithSameNames_exception() {
        String publishListName = "PL-1";
        contentFacade.createPublishList(publishListName, Instant.now());
        assertThrows(
                PublishListAlreadyExistsException.class,
                () -> contentFacade.createPublishList(publishListName, Instant.now())
        );
    }

    @Test
    void createBundlesWithDifferentIds_ok() {
        String publishListName = "PL-1";
        contentFacade.createPublishList(publishListName, Instant.now());
        contentFacade.createBundle(
                new ContentDistributionService.PublishListId(publishListName),
                new ContentDistributionService.BundleId("B", "1"),
                new byte[1]
        );
        contentFacade.createBundle(
                new ContentDistributionService.PublishListId(publishListName),
                new ContentDistributionService.BundleId("B", "2"),
                new byte[1]
        );
    }

    @Test
    void createBundlesWithSameIds_exception() {
        String publishListName = "PL-1";
        contentFacade.createPublishList(publishListName, Instant.now());
        contentFacade.createBundle(
                new ContentDistributionService.PublishListId(publishListName),
                new ContentDistributionService.BundleId("B", "1"),
                new byte[1]
        );
        assertThrows(
                BundleAlreadyPresentException.class,
                () -> contentFacade.createBundle(
                        new ContentDistributionService.PublishListId(publishListName),
                        new ContentDistributionService.BundleId("B", "1"),
                        new byte[1]
                ));
    }

    @Test
    void findActualBundles_ok() {
        String publishListName = "PL-1";
        contentFacade.createPublishList(publishListName, Instant.now());
        contentFacade.createBundle(
                new ContentDistributionService.PublishListId(publishListName),
                new ContentDistributionService.BundleId("B", "1"),
                new byte[1]
        );
        timeService.setNow(Instant.now().plusSeconds(1));
        Map<ContentDistributionService.BundleId, ContentDistributionServiceAdditional.UniqueBundleId> actualBundles = contentFacade
                .findActualBundles(Collections.singleton(new ContentDistributionService.BundleId("B", "1")));
        assertEquals(1, actualBundles.size());
    }

    @Test
    void findActualBundles_notFound() {
        String publishListName = "PL-1";
        contentFacade.createPublishList(publishListName, Instant.now());
        contentFacade.createBundle(
                new ContentDistributionService.PublishListId(publishListName),
                new ContentDistributionService.BundleId("B", "1"),
                new byte[1]
        );
        timeService.setNow(Instant.now().minusSeconds(1));
        Map<ContentDistributionService.BundleId, ContentDistributionServiceAdditional.UniqueBundleId> actualBundles = contentFacade
                .findActualBundles(Collections.singleton(new ContentDistributionService.BundleId("B", "1")));
        assertEquals(0, actualBundles.size());
    }
}