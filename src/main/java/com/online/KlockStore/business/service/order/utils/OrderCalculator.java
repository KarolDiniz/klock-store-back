package com.online.KlockStore.business.service.order.utils;

import com.online.KlockStore.model.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderCalculator {

    private static final double DESCONTO_VIP = 0.1;

    public double calcularTotal(Order pedido) {
        return pedido.getItems().stream()
                .mapToDouble(item -> item.getPreco() * item.getQuantidade())
                .sum();
    }

    public double calcularDesconto(Order pedido, double total) {
        return pedido.getCliente().isVip() ? total * (1 - DESCONTO_VIP) : total;
    }
}

