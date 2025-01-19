package com.online.KlockStore.business.exception.item;

import com.online.KlockStore.business.exception.api.ApiException;

public class ItemNotFoundException extends ApiException {
    public ItemNotFoundException(String message) {
        super(message, 404); // 404 - Not Found
    }
}
