package com.online.KlockStore.business.service.order.utils;

import com.online.KlockStore.business.exception.item.InsufficientStockException;
import com.online.KlockStore.business.exception.order.NotificationException;
import com.online.KlockStore.business.service.order.utils.notification.NotificationService;
import com.online.KlockStore.model.entities.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderProcessingService {

    private final OrderValidator orderValidator;
    private final OrderCalculator orderCalculator;
    private final OrderStockService orderStockService;
    private final NotificationService notificationService;

    public OrderProcessingService(
            OrderValidator orderValidator,
            OrderCalculator orderCalculator,
            OrderStockService orderStockService,
            NotificationService notificationService) {
        this.orderValidator = orderValidator;
        this.orderCalculator = orderCalculator;
        this.orderStockService = orderStockService;
        this.notificationService = notificationService;
    }

    public void processarPedido(Order pedido) {
        validarPedido(pedido);
        double total = calcularTotal(pedido);
        double totalComDesconto = calcularDesconto(pedido, total);
        verificarEstoque(pedido);

        definirDataEntrega(pedido);
        enviarNotificacao(pedido);
    }

    private void validarPedido(Order pedido) {
        orderValidator.validar(pedido);
    }

    private double calcularTotal(Order pedido) {
        double total = orderCalculator.calcularTotal(pedido);
        pedido.setTotal(total);
        return total;
    }

    private double calcularDesconto(Order pedido, double total) {
        double totalComDesconto = orderCalculator.calcularDesconto(pedido, total);
        pedido.setTotalComDesconto(totalComDesconto);
        return totalComDesconto;
    }

    private void verificarEstoque(Order pedido) {
        if (!orderStockService.verificarEstoque(pedido)) {
            throw new InsufficientStockException("Estoque insuficiente para os itens do pedido.");
        }
        pedido.setEmEstoque(true);
    }

    private void definirDataEntrega(Order pedido) {
        if (pedido.isEmEstoque()) {
            pedido.setDataEntrega(LocalDate.now().plusDays(OrderStockService.DIAS_ENTREGA));
        } else {
            pedido.setDataEntrega(null);
        }
    }

    private void enviarNotificacao(Order pedido) {
        try {
            notificationService.enviarNotificacao(pedido);
        } catch (NotificationException e) {
            throw new NotificationException("Erro ao enviar notificação para o pedido: " + pedido.getId());

        }
    }
}
