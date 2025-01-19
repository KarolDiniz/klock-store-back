package com.online.KlockStore.business.service.order.utils;

import com.online.KlockStore.model.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderStockService {

    public static final int DIAS_ENTREGA = 3;

    public boolean verificarEstoque(Order pedido) {
        return pedido.getItems().stream()
                .allMatch(item -> item.getQuantidade() <= item.getEstoque());
    }
}

