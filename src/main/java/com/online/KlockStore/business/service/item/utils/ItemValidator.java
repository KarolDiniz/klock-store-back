package com.online.KlockStore.business.service.item.utils;

import com.online.KlockStore.business.exception.item.ItemValidationException;
import com.online.KlockStore.model.entities.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
}
