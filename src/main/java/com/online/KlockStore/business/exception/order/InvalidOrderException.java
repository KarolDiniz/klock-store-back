package com.online.KlockStore.business.exception.order;

import com.online.KlockStore.business.exception.api.ApiException;

public class InvalidOrderException extends ApiException {
    public InvalidOrderException(String message) {
        super(message, 400); // 400 - Bad Request
    }
}
