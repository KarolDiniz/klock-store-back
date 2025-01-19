package com.online.KlockStore.business.exception.order;

import com.online.KlockStore.business.exception.api.ApiException;

public class NotificationException extends ApiException {
    public NotificationException(String message) {
        super(message, 500);
    }
}

