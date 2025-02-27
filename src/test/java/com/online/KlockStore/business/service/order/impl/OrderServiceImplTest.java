package com.online.KlockStore.business.service.order.impl;

import com.online.KlockStore.business.exception.order.OrderNotFoundException;
import com.online.KlockStore.business.service.customer.CustomerService;
import com.online.KlockStore.business.service.item.ItemService;
import com.online.KlockStore.business.service.order.utils.OrderProcessingService;
import com.online.KlockStore.model.entities.Customer;
import com.online.KlockStore.model.entities.Item;
import com.online.KlockStore.model.entities.Order;
import com.online.KlockStore.model.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService clienteService;

    @Mock
    private ItemService itemService;

    @Mock
    private OrderProcessingService pedidoProcessingService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private Customer customer;
    private List<Item> items;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);

        Item item = new Item();
        item.setId(1L);
        items = List.of(item);

        order = new Order();
        order.setId(1L);
        order.setCliente(customer);
        order.setItems(items);
    }

    @Test
    void testListarTodos() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> orders = orderService.listarTodos();

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.buscarPorId(1L);

        assertNotNull(foundOrder);
        assertEquals(order.getId(), foundOrder.getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorId_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.buscarPorId(1L));
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testCriarPedido() {
        when(clienteService.buscarPorId(1L)).thenReturn(customer);
        when(itemService.associarItens(items)).thenReturn(items);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.criarPedido(order);

        assertNotNull(createdOrder);
        assertEquals(order.getId(), createdOrder.getId());
        verify(clienteService, times(1)).buscarPorId(1L);
        verify(itemService, times(1)).associarItens(items);
        verify(orderRepository, times(1)).save(order);
        verify(pedidoProcessingService, times(1)).processarPedido(order);
    }

    @Test
    void testAtualizarPedido() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(clienteService.buscarPorId(1L)).thenReturn(customer);
        when(itemService.associarItens(items)).thenReturn(items);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.atualizarPedido(1L, order);

        assertNotNull(updatedOrder);
        assertEquals(order.getId(), updatedOrder.getId());
        verify(orderRepository, times(1)).findById(1L);
        verify(clienteService, times(1)).buscarPorId(1L);
        verify(itemService, times(1)).associarItens(items);
        verify(orderRepository, times(1)).save(order);
        verify(pedidoProcessingService, times(1)).processarPedido(order);
    }

    @Test
    void testExcluirPedido() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.excluirPedido(1L);

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void testExcluirPedido_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.excluirPedido(1L));
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testCriarPedido_FalhaAssociarItens() {
        when(clienteService.buscarPorId(1L)).thenReturn(customer);
        when(itemService.associarItens(items)).thenThrow(new RuntimeException("Erro ao associar itens"));

        assertThrows(RuntimeException.class, () -> orderService.criarPedido(order));
        verify(itemService, times(1)).associarItens(items);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testAtualizarPedido_FalhaAssociarItens() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(clienteService.buscarPorId(1L)).thenReturn(customer);
        when(itemService.associarItens(items)).thenThrow(new RuntimeException("Erro ao associar itens"));

        assertThrows(RuntimeException.class, () -> orderService.atualizarPedido(1L, order));
        verify(itemService, times(1)).associarItens(items);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testExcluirPedido_PedidoNaoEncontrado() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.excluirPedido(1L));
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).delete(any(Order.class));
    }

    @Test
    void testCriarPedido_Sucesso() {
        when(clienteService.buscarPorId(customer.getId())).thenReturn(customer);
        when(itemService.associarItens(order.getItems())).thenReturn(items);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.criarPedido(order);
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testListarTodos_EmptyList() {
        when(orderRepository.findAll()).thenReturn(List.of());

        List<Order> orders = orderService.listarTodos();

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testCriarPedido_ClienteNaoEncontrado() {
        when(clienteService.buscarPorId(1L)).thenThrow(new RuntimeException("Cliente não encontrado"));

        assertThrows(RuntimeException.class, () -> orderService.criarPedido(order));
        verify(clienteService, times(1)).buscarPorId(1L);
        verify(itemService, never()).associarItens(anyList());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCriarPedido_ValidacaoDadosNulos() {
        order.setCliente(null);

        assertThrows(NullPointerException.class, () -> orderService.criarPedido(order));
        verify(clienteService, never()).buscarPorId(anyLong());
        verify(itemService, never()).associarItens(anyList());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testAtualizarPedido_ClienteNaoEncontrado() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(clienteService.buscarPorId(1L)).thenThrow(new RuntimeException("Cliente não encontrado"));

        assertThrows(RuntimeException.class, () -> orderService.atualizarPedido(1L, order));
        verify(clienteService, times(1)).buscarPorId(1L);
        verify(itemService, never()).associarItens(anyList());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testProcessarPedido_NaoProcessado() {
        doThrow(new RuntimeException("Erro ao processar pedido"))
                .when(pedidoProcessingService)
                .processarPedido(order);

        when(clienteService.buscarPorId(1L)).thenReturn(customer);
        when(itemService.associarItens(items)).thenReturn(items);

        assertThrows(RuntimeException.class, () -> orderService.criarPedido(order));
        verify(pedidoProcessingService, times(1)).processarPedido(order);
        verify(orderRepository, never()).save(any(Order.class));
    }

}

