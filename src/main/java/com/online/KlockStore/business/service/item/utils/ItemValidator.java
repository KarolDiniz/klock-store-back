package com.online.KlockStore.business.service.item.utils;

import com.online.KlockStore.business.exception.item.ItemValidationException;
import com.online.KlockStore.model.entities.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class ItemValidator {

    public void validarItem(Item item) {
        if (item == null) {
            throw new ItemValidationException("O item não pode ser nulo.");
        }
        if (!StringUtils.hasText(item.getNome())) {
            throw new ItemValidationException("O nome do item não pode estar em branco.");
        }
        if (item.getPreco() < 0) {
            throw new ItemValidationException("O preço do item não pode ser negativo.");
        }
        if (item.getQuantidade() < 0) {
            throw new ItemValidationException("A quantidade do item não pode ser negativa.");
        }
        if (item.getEstoque() < 0) {
            throw new ItemValidationException("O estoque do item não pode ser negativo.");
        }
    }

    public void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
    }

    public void validarItensPedido(List<Item> itensPedido) {
        if (itensPedido == null || itensPedido.isEmpty()) {
            throw new IllegalArgumentException("A lista de itens do pedido não pode ser nula ou vazia.");
        }
    }
}
