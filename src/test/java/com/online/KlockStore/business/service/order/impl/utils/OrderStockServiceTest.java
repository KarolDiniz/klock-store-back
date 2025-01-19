package com.online.KlockStore.business.service.order.impl.utils;
import com.online.KlockStore.business.service.order.utils.OrderStockService;
import com.online.KlockStore.model.entities.Item;
import com.online.KlockStore.model.entities.Order;
import com.online.KlockStore.model.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class OrderStockServiceTest {

    private OrderStockService orderStockService;
    private Order order;

    @BeforeEach
    void setUp() {
        orderStockService = new OrderStockService();
    }

    @Test
    void testVerificarEstoqueSuficiente() {
        Item item1 = new Item("Produto 1", 10.0, 5, 10);  // Preço: 10.0, Quantidade: 5, Estoque: 10
        Item item2 = new Item("Produto 2", 20.0, 2, 3);   // Preço: 20.0, Quantidade: 2, Estoque: 3

        Customer cliente = new Customer("Cliente 1", false);
        order = new Order(cliente, Arrays.asList(item1, item2));

        boolean estoqueSuficiente = orderStockService.verificarEstoque(order);

        assertTrue(estoqueSuficiente);
    }

    @Test
    void testVerificarEstoqueInsuficiente() {
        Item item1 = new Item("Produto 1", 10.0, 5, 4);  // Preço: 10.0, Quantidade: 5, Estoque: 4
        Item item2 = new Item("Produto 2", 20.0, 2, 3);  // Preço: 20.0, Quantidade: 2, Estoque: 3

        Customer cliente = new Customer("Cliente 1", false);
        order = new Order(cliente, Arrays.asList(item1, item2));

        boolean estoqueSuficiente = orderStockService.verificarEstoque(order);

        assertFalse(estoqueSuficiente);
    }

    @Test
    void testVerificarEstoqueQuantidadeIgualEstoque() {
        Item item1 = new Item("Produto 1", 10.0, 5, 5);  // Preço: 10.0, Quantidade: 5, Estoque: 5
        Item item2 = new Item("Produto 2", 20.0, 2, 2);  // Preço: 20.0, Quantidade: 2, Estoque: 2

        Customer cliente = new Customer("Cliente 1", false);
        order = new Order(cliente, Arrays.asList(item1, item2));

        boolean estoqueSuficiente = orderStockService.verificarEstoque(order);
        assertTrue(estoqueSuficiente);
    }

    @Test
    void testVerificarEstoquePedidoVazio() {
        Customer cliente = new Customer("Cliente 1", false);
        order = new Order(cliente, Arrays.asList());

        boolean estoqueSuficiente = orderStockService.verificarEstoque(order);
        assertTrue(estoqueSuficiente);
    }

    @Test
    void testVerificarEstoqueComQuantidadeMaiorQueEstoque() {
        Item item1 = new Item("Produto 1", 10.0, 10, 5);  // Preço: 10.0, Quantidade: 10, Estoque: 5
        Item item2 = new Item("Produto 2", 20.0, 2, 3);   // Preço: 20.0, Quantidade: 2, Estoque: 3

        Customer cliente = new Customer("Cliente 1", false);
        order = new Order(cliente, Arrays.asList(item1, item2));

        boolean estoqueSuficiente = orderStockService.verificarEstoque(order);

        assertFalse(estoqueSuficiente);
    }
}

