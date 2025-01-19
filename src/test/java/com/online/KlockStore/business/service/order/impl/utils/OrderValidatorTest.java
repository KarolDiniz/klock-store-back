package com.online.KlockStore.business.service.order.impl.utils;

import com.online.KlockStore.business.exception.order.InvalidOrderException;
import com.online.KlockStore.business.service.order.utils.OrderValidator;
import com.online.KlockStore.model.entities.Item;
import com.online.KlockStore.model.entities.Order;
import com.online.KlockStore.model.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class OrderValidatorTest {

    private OrderValidator orderValidator;
    private Order order;

    @BeforeEach
    void setUp() {
        orderValidator = new OrderValidator();
    }

    @Test
    void testValidarPedidoValido() {
        Item item1 = new Item("Produto 1", 10.0, 5, 10);  // Preço: 10.0, Quantidade: 5, Estoque: 10
        Item item2 = new Item("Produto 2", 20.0, 2, 3);   // Preço: 20.0, Quantidade: 2, Estoque: 3

        Customer cliente = new Customer("Cliente 1", false);
        order = new Order(cliente, Arrays.asList(item1, item2));

        orderValidator.validar(order);

    }

    @Test
    void testValidarPedidoSemItens() {
        Customer cliente = new Customer("Cliente 1", false);
        order = new Order(cliente, Arrays.asList());

        InvalidOrderException exception = assertThrows(InvalidOrderException.class, () -> {
            orderValidator.validar(order);
        });

        assertEquals("O pedido é inválido. Certifique-se de que possui itens.", exception.getMessage());
    }

    @Test
    void testValidarPedidoNulo() {
        InvalidOrderException exception = assertThrows(InvalidOrderException.class, () -> {
            orderValidator.validar(null);
        });

        assertEquals("O pedido é inválido. Certifique-se de que possui itens.", exception.getMessage());
    }

    @Test
    void testValidarPedidoComItensNulos() {
        Customer cliente = new Customer("Cliente 1", false);
        order = new Order(cliente, null);  // Itens são nulos

        InvalidOrderException exception = assertThrows(InvalidOrderException.class, () -> {
            orderValidator.validar(order);
        });

        assertEquals("O pedido é inválido. Certifique-se de que possui itens.", exception.getMessage());
    }
}

