package com.online.KlockStore.business.service.item;
import com.online.KlockStore.model.entities.Item;
import java.util.List;

public interface ItemService {
    Item buscarPorId(Long id);

    List<Item> associarItens(List<Item> itensPedido);

    List<Item> listarTodos();

    Item salvarItem(Item item);

    Item atualizarItem(Long id, Item itemAtualizado);

    void excluirItem(Long id);
}