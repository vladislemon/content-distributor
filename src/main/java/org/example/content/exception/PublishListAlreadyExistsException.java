package org.example.content.exception;

import org.example.ContentDistributionService;

public class PublishListAlreadyExistsException extends RuntimeException {

    public PublishListAlreadyExistsException(ContentDistributionService.PublishListId publishListId) {
        super(String.format("Publish list [%s] already exists", publishListId));
    }
}
