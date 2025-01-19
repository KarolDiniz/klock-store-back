package com.online.KlockStore.business.service.order;
import com.online.KlockStore.model.entities.Order;
import java.util.List;

public interface OrderService {

    List<Order> listarTodos();

    Order buscarPorId(Long id);

    Order criarPedido(Order pedido);

    Order atualizarPedido(Long id, Order pedidoAtualizado);

    void excluirPedido(Long id);

}