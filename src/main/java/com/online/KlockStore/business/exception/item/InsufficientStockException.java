package com.online.KlockStore.business.exception.item;

import com.online.KlockStore.business.exception.api.ApiException;

public class InsufficientStockException extends ApiException {
    public InsufficientStockException(String message) {
        super(message, 400); // 400 - Bad Request
    }

}
