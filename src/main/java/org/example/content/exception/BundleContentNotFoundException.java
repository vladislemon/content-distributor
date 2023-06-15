package org.example.content.exception;

import org.example.ContentDistributionServiceAdditional;

public class BundleContentNotFoundException extends RuntimeException {

    public BundleContentNotFoundException(ContentDistributionServiceAdditional.UniqueBundleId uniqueBundleId) {
        super(String.format("Bundle content [%s] not found", uniqueBundleId));
    }
}
