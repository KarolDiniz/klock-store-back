package com.online.KlockStore.presentation.controller;

import com.online.KlockStore.business.service.order.OrderService;
import com.online.KlockStore.model.entities.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService pedidoService;

    @InjectMocks
    private OrderController orderController;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setId(1L);
    }

    @Test
    void testListarTodos() {
        List<Order> pedidos = Arrays.asList(order, new Order());
        when(pedidoService.listarTodos()).thenReturn(pedidos);

        List<Order> resultado = orderController.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pedidoService).listarTodos();
    }

    @Test
    void testBuscarPorId_Sucesso() {
        when(pedidoService.buscarPorId(1L)).thenReturn(order);

        ResponseEntity<Order> response = orderController.buscarPorId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(order, response.getBody());
        verify(pedidoService).buscarPorId(1L);
    }

    @Test
    void testBuscarPorId_NaoEncontrado() {
        when(pedidoService.buscarPorId(1L)).thenThrow(new RuntimeException("Pedido não encontrado"));

        ResponseEntity<Order> response = orderController.buscarPorId(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(pedidoService).buscarPorId(1L);
    }

    @Test
    void testCriarPedido() {
        when(pedidoService.criarPedido(order)).thenReturn(order);

        ResponseEntity<Order> response = orderController.criarPedido(order);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(order, response.getBody());
        verify(pedidoService).criarPedido(order);
    }

    @Test
    void testAtualizarPedido_Sucesso() {
        Order pedidoAtualizado = new Order();
        pedidoAtualizado.setId(1L);
        when(pedidoService.atualizarPedido(1L, pedidoAtualizado)).thenReturn(pedidoAtualizado);

        ResponseEntity<Order> response = orderController.atualizarPedido(1L, pedidoAtualizado);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(pedidoAtualizado, response.getBody());
        verify(pedidoService).atualizarPedido(1L, pedidoAtualizado);
    }

    @Test
    void testAtualizarPedido_NaoEncontrado() {
        Order pedidoAtualizado = new Order();
        pedidoAtualizado.setId(1L);
        when(pedidoService.atualizarPedido(1L, pedidoAtualizado)).thenThrow(new RuntimeException("Pedido não encontrado"));

        ResponseEntity<Order> response = orderController.atualizarPedido(1L, pedidoAtualizado);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(pedidoService).atualizarPedido(1L, pedidoAtualizado);
    }

    @Test
    void testExcluirPedido_Sucesso() {
        doNothing().when(pedidoService).excluirPedido(1L);

        ResponseEntity<Void> response = orderController.excluirPedido(1L);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(pedidoService).excluirPedido(1L);
    }

    @Test
    void testExcluirPedido_NaoEncontrado() {
        doThrow(new RuntimeException("Pedido não encontrado")).when(pedidoService).excluirPedido(1L);

        ResponseEntity<Void> response = orderController.excluirPedido(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(pedidoService).excluirPedido(1L);
    }

    @Test
    void testListarTodos_Vazio() {
        when(pedidoService.listarTodos()).thenReturn(Collections.emptyList());

        List<Order> resultado = orderController.listarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(pedidoService).listarTodos();
    }


    @Test
    void testCriarPedido_Null() {
        when(pedidoService.criarPedido(null)).thenThrow(new IllegalArgumentException("Pedido não pode ser nulo"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderController.criarPedido(null));

        assertEquals("Pedido não pode ser nulo", exception.getMessage());
        verify(pedidoService).criarPedido(null);
    }

}

