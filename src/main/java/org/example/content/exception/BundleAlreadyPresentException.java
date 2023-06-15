package org.example.content.exception;

import org.example.ContentDistributionService;

public class BundleAlreadyPresentException extends RuntimeException {

    public BundleAlreadyPresentException(
            ContentDistributionService.PublishListId publishListId,
            ContentDistributionService.BundleId bundleId
    ) {
        super(String.format("Bundle [%s] already present in publish list [%s]", bundleId, publishListId));
    }
}
