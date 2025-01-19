package com.online.KlockStore.business.service.order.utils.notification;

import com.online.KlockStore.model.entities.Order;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void enviarNotificacao(Order pedido) {
        String email = pedido.getCliente().getEmail();
        String mensagem = pedido.isEmEstoque()
                ? "Seu pedido será entregue em breve."
                : "Um ou mais itens do seu pedido estão fora de estoque.";
        System.out.println("Enviando e-mail para " + email + ": " + mensagem);
    }
}
