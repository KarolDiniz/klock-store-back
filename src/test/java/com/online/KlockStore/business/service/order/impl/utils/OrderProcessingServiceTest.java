package com.online.KlockStore.business.service.order.impl.utils;

import com.online.KlockStore.business.exception.item.InsufficientStockException;
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
import java.util.Arrays;
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
        when(estoqueService.verificarEstoque(order)).thenReturn(true);
        doNothing().when(notificacaoService).enviarNotificacao(order);

        orderProcessingService.processarPedido(order);

        assertEquals(100.0, order.getTotal());
        assertEquals(90.0, order.getTotalComDesconto());
        assertTrue(order.isEmEstoque());
        assertNotNull(order.getDataEntrega());
        verify(pedidoValidator).validar(order);
        verify(pedidoCalculator).calcularTotal(order);
        verify(pedidoCalculator).calcularDesconto(order, 100.0);
        verify(estoqueService).verificarEstoque(order);
        verify(notificacaoService).enviarNotificacao(order);
    }

    @Test
    void testProcessarPedido_DataEntrega() {
        order.setEmEstoque(true);
        when(estoqueService.verificarEstoque(order)).thenReturn(true);

        orderProcessingService.processarPedido(order);

        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(OrderStockService.DIAS_ENTREGA);
        assertEquals(expectedDeliveryDate, order.getDataEntrega());
    }
    @Test
    void testProcessarPedido_CalculoDescontoComTotalZero() {
        order.setItems(Collections.singletonList(new Item()));
        when(pedidoCalculator.calcularTotal(order)).thenReturn(0.0);
        when(pedidoCalculator.calcularDesconto(order, 0.0)).thenReturn(0.0);
        when(estoqueService.verificarEstoque(order)).thenReturn(true);
        doNothing().when(notificacaoService).enviarNotificacao(order);

        orderProcessingService.processarPedido(order);

        assertEquals(0.0, order.getTotal());
        assertEquals(0.0, order.getTotalComDesconto());
        assertNotNull(order.getDataEntrega());
    }

    @Test
    void testProcessarPedido_ItemComPrecoZero() {
        Item item = new Item();
        item.setPreco(0.0);
        order.setItems(Collections.singletonList(item));

        when(pedidoCalculator.calcularTotal(order)).thenReturn(0.0);
        when(pedidoCalculator.calcularDesconto(order, 0.0)).thenReturn(0.0);
        when(estoqueService.verificarEstoque(order)).thenReturn(true);
        doNothing().when(notificacaoService).enviarNotificacao(order);

        orderProcessingService.processarPedido(order);

        assertEquals(0.0, order.getTotal());
        assertEquals(0.0, order.getTotalComDesconto());
        assertNotNull(order.getDataEntrega());
    }

    @Test
    void testProcessarPedido_EstoqueInsuficiente() {
        when(estoqueService.verificarEstoque(order)).thenReturn(false);

        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () -> {
            orderProcessingService.processarPedido(order);
        });

        assertEquals("Estoque insuficiente para os itens do pedido.", exception.getMessage());
        verify(estoqueService).verificarEstoque(order);
    }

    @Test
    void testProcessarPedido_NotificacaoFalha() {
        when(estoqueService.verificarEstoque(order)).thenReturn(true);
        doThrow(new NotificationException("Erro ao enviar notificação.")).when(notificacaoService).enviarNotificacao(order);

        NotificationException exception = assertThrows(NotificationException.class, () -> {
            orderProcessingService.processarPedido(order);
        });

        assertEquals("Erro ao enviar notificação para o pedido: 1", exception.getMessage());
        verify(notificacaoService).enviarNotificacao(order);
    }

    @Test
    void testProcessarPedido_PedidoInvalido() {
        doThrow(new IllegalArgumentException("Pedido inválido.")).when(pedidoValidator).validar(order);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderProcessingService.processarPedido(order);
        });

        assertEquals("Pedido inválido.", exception.getMessage());
        verify(pedidoValidator).validar(order);
    }

    @Test
    void testProcessarPedido_PedidoSemItens() {
        order.setItems(Collections.emptyList());

        doThrow(new IllegalArgumentException("Pedido sem itens.")).when(pedidoValidator).validar(order);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderProcessingService.processarPedido(order);
        });

        assertEquals("Pedido sem itens.", exception.getMessage());
        verify(pedidoValidator).validar(order);
    }

    @Test
    void testProcessarPedido_PedidoComDatasPredefinidas() {
        order.setDataEntrega(LocalDate.now().plusDays(5));

        when(estoqueService.verificarEstoque(order)).thenReturn(true);

        orderProcessingService.processarPedido(order);

        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(OrderStockService.DIAS_ENTREGA);
        assertEquals(expectedDeliveryDate, order.getDataEntrega());
    }

    @Test
    void testProcessarPedido_CalculoTotalComMultiplosItens() {
        Item item1 = new Item();
        item1.setPreco(50.0);
        Item item2 = new Item();
        item2.setPreco(30.0);
        order.setItems(Arrays.asList(item1, item2));

        when(pedidoCalculator.calcularTotal(order)).thenReturn(80.0);
        when(pedidoCalculator.calcularDesconto(order, 80.0)).thenReturn(72.0);
        when(estoqueService.verificarEstoque(order)).thenReturn(true);

        orderProcessingService.processarPedido(order);

        assertEquals(80.0, order.getTotal());
        assertEquals(72.0, order.getTotalComDesconto());
        assertNotNull(order.getDataEntrega());
    }

    @Test
    void testProcessarPedido_SemDefinirNotificacao() {
        order.setDataEntrega(null);

        when(pedidoCalculator.calcularTotal(order)).thenReturn(100.0);
        when(pedidoCalculator.calcularDesconto(order, 100.0)).thenReturn(95.0);
        when(estoqueService.verificarEstoque(order)).thenReturn(true);

        doThrow(new NotificationException("Serviço de notificação não configurado.")).when(notificacaoService).enviarNotificacao(order);
        NotificationException exception = assertThrows(NotificationException.class, () -> {
            orderProcessingService.processarPedido(order);
        });
        assertEquals("Erro ao enviar notificação para o pedido: 1", exception.getMessage());
    }

    @Test
    void testProcessarPedido_QuantidadeNegativa() {
        Item item = new Item();
        item.setPreco(50.0);
        item.setQuantidade(-5);
        order.setItems(Collections.singletonList(item));

        doThrow(new IllegalArgumentException("Quantidade de itens inválida.")).when(pedidoValidator).validar(order);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderProcessingService.processarPedido(order);
        });

        assertEquals("Quantidade de itens inválida.", exception.getMessage());
        verify(pedidoValidator).validar(order);
    }
    @Test
    void testProcessarPedido_ItensNulos() {
        order.setItems(Arrays.asList(null, new Item()));
        doThrow(new IllegalArgumentException("Pedido contém itens inválidos.")).when(pedidoValidator).validar(order);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderProcessingService.processarPedido(order);
        });

        assertEquals("Pedido contém itens inválidos.", exception.getMessage());
        verify(pedidoValidator).validar(order);
    }

    @Test
    void testProcessarPedido_NotificacaoAssincrona() {
        when(estoqueService.verificarEstoque(order)).thenReturn(true);
        doAnswer(invocation -> {
            Order pedido = invocation.getArgument(0);
            assertNotNull(pedido);
            return null;
        }).when(notificacaoService).enviarNotificacao(order);
        orderProcessingService.processarPedido(order);
        verify(notificacaoService).enviarNotificacao(order);
    }


}

