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
    private final CustomerService clienteService;
    private final ItemService itemService;
    private final OrderProcessingService pedidoProcessingService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerService clienteService,
                            ItemService itemService,
                            OrderProcessingService pedidoProcessingService) {
        this.orderRepository = orderRepository;
        this.clienteService = clienteService;
        this.itemService = itemService;
        this.pedidoProcessingService = pedidoProcessingService;
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
    public Order criarPedido(Order pedido) {
        if (pedido.getCliente() == null) {
            throw new OrderNotFoundException("Itens não podem ser nulos ou vazios");
        }

        Customer cliente = clienteService.buscarPorId(pedido.getCliente().getId());
        if (cliente == null) {
            throw new OrderNotFoundException("Itens não podem ser nulos ou vazios");
        }
        pedido.setCliente(cliente);

        if (pedido.getItems() == null || pedido.getItems().isEmpty()) {
            throw new OrderNotFoundException("Itens não podem ser nulos ou vazios");
        }

        List<Item> itens = itemService.associarItens(pedido.getItems());
        pedido.setItems(itens);

        pedidoProcessingService.processarPedido(pedido);
        return orderRepository.save(pedido);
    }


    @Override
    public Order atualizarPedido(Long id, Order pedidoAtualizado) {
        Order pedidoExistente = buscarPorId(id);
        Customer cliente = clienteService.buscarPorId(pedidoAtualizado.getCliente().getId());
        pedidoExistente.setCliente(cliente);
        List<Item> itens = itemService.associarItens(pedidoAtualizado.getItems());
        pedidoExistente.setItems(itens);
        pedidoProcessingService.processarPedido(pedidoExistente);
        return orderRepository.save(pedidoExistente);
    }

    @Override
    public void excluirPedido(Long id) {
        Order pedido = buscarPorId(id);
        orderRepository.delete(pedido);
    }
}
