package org.example.content.exception;

import org.example.ContentDistributionService;

public class PublishListNotExistsException extends RuntimeException {

    public PublishListNotExistsException(ContentDistributionService.PublishListId publishListId) {
        super(String.format("Publish list [%s] does not exists", publishListId));
    }
}
