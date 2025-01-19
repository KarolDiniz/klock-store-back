package com.online.KlockStore.business.service.item.impl;
import com.online.KlockStore.business.exception.item.ItemNotFoundException;
import com.online.KlockStore.business.service.item.ItemService;
import com.online.KlockStore.model.entities.Item;
import com.online.KlockStore.model.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item buscarPorId(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item não encontrado!"));
    }

    @Override
    public List<Item> associarItens(List<Item> itensPedido) {
        return itensPedido.stream()
                .map(this::buscarOuCriarItem)
                .collect(Collectors.toList());
    }

    private Item buscarOuCriarItem(Item item) {
        Item itemExistente = buscarPorId(item.getId());
        return (itemExistente != null) ? itemExistente : salvarItem(item);
    }

    @Override
    public List<Item> listarTodos() {
        return itemRepository.findAll();
    }

    @Override
    public Item salvarItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void excluirItem(Long id) {
        Item item = buscarPorId(id);
        itemRepository.delete(item);
    }
}

