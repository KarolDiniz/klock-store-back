package com.online.KlockStore.business.service.order.impl;

import com.online.KlockStore.business.exception.order.OrderNotFoundException;
import com.online.KlockStore.business.service.order.OrderService;
import com.online.KlockStore.model.entities.Customer;
import com.online.KlockStore.model.entities.Item;
import com.online.KlockStore.model.entities.Order;
import com.online.KlockStore.model.repository.OrderRepository;
import com.online.KlockStore.business.service.customer.CustomerService;
import com.online.KlockStore.business.service.item.ItemService;
import com.online.KlockStore.business.service.order.utils.OrderProcessingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final OrderProcessingService orderProcessingService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerService customerService,
                            ItemService itemService,
                            OrderProcessingService orderProcessingService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.itemService = itemService;
        this.orderProcessingService = orderProcessingService;
    }

    @Override
    public List<Order> listarTodos() {
        return orderRepository.findAll();
    }

    @Override
    public Order buscarPorId(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado!"));
    }

    @Override
    public Order criarPedido(Order order) {
        configurarPedido(order);
        processarPedido(order);
        return orderRepository.save(order);
    }

    @Override
    public Order atualizarPedido(Long id, Order updatedOrder) {
        Order existingOrder = buscarPorId(id);
        configurarPedidoAtualizado(existingOrder, updatedOrder);
        processarPedido(existingOrder);
        return orderRepository.save(existingOrder);
    }

    @Override
    public void excluirPedido(Long id) {
        Order order = buscarPorId(id);
        orderRepository.delete(order);
    }

    private void configurarPedido(Order order) {
        Customer customer = customerService.buscarPorId(order.getCliente().getId());
        order.setCliente(customer);
        List<Item> items = itemService.associarItens(order.getItems());
        order.setItems(items);
    }

    private void configurarPedidoAtualizado(Order existingOrder, Order updatedOrder) {
        configurarPedido(updatedOrder);
        existingOrder.setCliente(updatedOrder.getCliente());
        existingOrder.setItems(updatedOrder.getItems());
    }

    private void processarPedido(Order order) {
        orderProcessingService.processarPedido(order);
    }
}
