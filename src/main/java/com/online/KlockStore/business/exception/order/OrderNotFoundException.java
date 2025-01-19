package com.online.KlockStore.business.exception.order;

import com.online.KlockStore.business.exception.api.ApiException;

public class OrderNotFoundException extends ApiException {
    public OrderNotFoundException(String message) {
        super(message, 404); // 404 - Not Found
    }
}