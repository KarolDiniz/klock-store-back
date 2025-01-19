package com.online.KlockStore.business.exception.customer;

import com.online.KlockStore.business.exception.api.ApiException;

public class CustomerNotFoundException extends ApiException {
    public CustomerNotFoundException(String message) {
        super(message, 404); // 404 - Not Found
    }
}
