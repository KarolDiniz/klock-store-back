package com.online.KlockStore.business.service.order.impl.utils;

import com.online.KlockStore.business.service.order.utils.OrderCalculator;
import com.online.KlockStore.model.entities.Customer;
import com.online.KlockStore.model.entities.Item;
import com.online.KlockStore.model.entities.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

class OrderCalculatorTest {

    private OrderCalculator orderCalculator;
    private Order order;

    @BeforeEach
    void setUp() {
        orderCalculator = new OrderCalculator();
    }

    @Test
    void testCalcularTotalSemDesconto() {
        Item item1 = new Item("Produto 1", 10.0, 2, 2);
        Item item2 = new Item("Produto 2", 20.0, 1, 2);
        Customer cliente = new Customer("Cliente 1", false);
        order = new Order(cliente, Arrays.asList(item1, item2));

        double total = orderCalculator.calcularTotal(order);

        assertEquals(40.0, total, 0.01);
    }

    @Test
    void testCalcularDescontoVip() {
        Item item1 = new Item("Produto 1", 10.0, 2, 2);
        Item item2 = new Item("Produto 2", 20.0, 1, 2);
        Customer cliente = new Customer("Cliente VIP", true);
        order = new Order(cliente, Arrays.asList(item1, item2));

        double total = orderCalculator.calcularTotal(order);
        double totalComDesconto = orderCalculator.calcularDesconto(order, total);

        assertEquals(36.0, totalComDesconto, 0.01);
    }

    @Test
    void testCalcularDescontoNaoVip() {
        Item item1 = new Item("Produto 1", 10.0, 2, 2);
        Item item2 = new Item("Produto 2", 20.0, 1, 1);
        Customer cliente = new Customer("Cliente Normal", false);
        order = new Order(cliente, Arrays.asList(item1, item2));

        double total = orderCalculator.calcularTotal(order);
        double totalComDesconto = orderCalculator.calcularDesconto(order, total);

        assertEquals(40.0, totalComDesconto, 0.01);
    }

    @Test
    void testCalcularTotalSemItens() {
        Customer cliente = new Customer("Cliente 1", false);
        order = new Order(cliente, Arrays.asList());

        double total = orderCalculator.calcularTotal(order);

        assertEquals(0.0, total, 0.01);
    }

    @Test
    void testCalcularDescontoSemItens() {
        Customer cliente = new Customer("Cliente VIP", true);
        order = new Order(cliente, Arrays.asList());

        double total = orderCalculator.calcularTotal(order);
        double totalComDesconto = orderCalculator.calcularDesconto(order, total);

        assertEquals(0.0, totalComDesconto, 0.01);
    }
}
