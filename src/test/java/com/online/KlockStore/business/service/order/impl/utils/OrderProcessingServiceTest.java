package com.online.KlockStore.business.service.order.impl.utils;

import com.online.KlockStore.business.exception.order.NotificationException;
import com.online.KlockStore.business.service.order.utils.OrderCalculator;
import com.online.KlockStore.business.service.order.utils.OrderProcessingService;
import com.online.KlockStore.business.service.order.utils.OrderStockService;
import com.online.KlockStore.business.service.order.utils.OrderValidator;
import com.online.KlockStore.business.service.order.utils.notification.NotificationService;
import com.online.KlockStore.model.entities.Item;
import com.online.KlockStore.model.entities.Order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderProcessingServiceTest {

    @Mock
    private OrderValidator pedidoValidator;

    @Mock
    private OrderCalculator pedidoCalculator;

    @Mock
    private OrderStockService estoqueService;

    @Mock
    private NotificationService notificacaoService;

    @InjectMocks
    private OrderProcessingService orderProcessingService;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order = new Order();
        order.setId(1L);
        order.setEmEstoque(true);
    }

    @Test
    void testProcessarPedido_Sucesso() {
        when(pedidoCalculator.calcularTotal(order)).thenReturn(100.0);
        when(pedidoCalculator.calcularDesconto(order, 100.0)).thenReturn(90.0);
        when(estoqueService.verificarEstoque(order)).thenReturn(true);  // Estoque suficiente
        doNothing().when(notificacaoService).enviarNotificacao(order);  // Notificação enviada com sucesso

        orderProcessingService.processarPedido(order);

        assertEquals(100.0, order.getTotal());
        assertEquals(90.0, order.getTotalComDesconto());
        assertTrue(order.isEmEstoque());
        assertNotNull(order.getDataEntrega());  // Data de entrega definida
        verify(pedidoValidator).validar(order);  // Verificando se o pedido foi validado
        verify(pedidoCalculator).calcularTotal(order);  // Verificando cálculo do total
        verify(pedidoCalculator).calcularDesconto(order, 100.0);  // Verificando cálculo do desconto
        verify(estoqueService).verificarEstoque(order);  // Verificando verificação do estoque
        verify(notificacaoService).enviarNotificacao(order);  // Verificando envio de notificação
    }

    @Test
    void testProcessarPedido_DataEntrega() {
        order.setEmEstoque(true);  // Garantindo que o pedido está em estoque
        when(estoqueService.verificarEstoque(order)).thenReturn(true);  // Estoque suficiente

        orderProcessingService.processarPedido(order);

        // Verificando se a data de entrega foi definida corretamente
        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(OrderStockService.DIAS_ENTREGA);
        assertEquals(expectedDeliveryDate, order.getDataEntrega());
    }
    @Test
    void testProcessarPedido_CalculoDescontoComTotalZero() {
        order.setItems(Collections.singletonList(new Item()));  // Item com valor zero
        when(pedidoCalculator.calcularTotal(order)).thenReturn(0.0);
        when(pedidoCalculator.calcularDesconto(order, 0.0)).thenReturn(0.0);
        when(estoqueService.verificarEstoque(order)).thenReturn(true);  // Estoque suficiente
        doNothing().when(notificacaoService).enviarNotificacao(order);  // Notificação enviada com sucesso

        orderProcessingService.processarPedido(order);

        assertEquals(0.0, order.getTotal());
        assertEquals(0.0, order.getTotalComDesconto());
        assertNotNull(order.getDataEntrega());  // Data de entrega definida
    }

    @Test
    void testProcessarPedido_ItemComPrecoZero() {
        Item item = new Item();
        item.setPreco(0.0);  // Definindo preço zero para o item
        order.setItems(Collections.singletonList(item));

        when(pedidoCalculator.calcularTotal(order)).thenReturn(0.0);
        when(pedidoCalculator.calcularDesconto(order, 0.0)).thenReturn(0.0);
        when(estoqueService.verificarEstoque(order)).thenReturn(true);  // Estoque suficiente
        doNothing().when(notificacaoService).enviarNotificacao(order);  // Notificação enviada com sucesso

        orderProcessingService.processarPedido(order);

        assertEquals(0.0, order.getTotal());  // Total deve ser zero
        assertEquals(0.0, order.getTotalComDesconto());  // Desconto deve ser zero
        assertNotNull(order.getDataEntrega());  // Data de entrega definida
    }
}

