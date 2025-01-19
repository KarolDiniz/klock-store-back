package com.online.KlockStore.business.service.order.utils;

import com.online.KlockStore.business.exception.order.InvalidOrderException;
import com.online.KlockStore.model.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public void validar(Order pedido) {
        if (pedido == null || pedido.getItems() == null || pedido.getItems().isEmpty()) {
            throw new InvalidOrderException("O pedido é inválido. Certifique-se de que possui itens.");
        }
    }
}

