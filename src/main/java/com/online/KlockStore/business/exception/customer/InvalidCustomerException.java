package com.online.KlockStore.business.exception.customer;

import com.online.KlockStore.business.exception.api.ApiException;

public class InvalidCustomerException extends ApiException {
    public InvalidCustomerException(String message) {
        super(message, 400);
}
}
