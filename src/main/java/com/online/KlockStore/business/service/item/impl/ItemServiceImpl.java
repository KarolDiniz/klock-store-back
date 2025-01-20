package com.online.KlockStore.business.service.item.impl;

import com.online.KlockStore.business.exception.item.ItemNotFoundException;
import com.online.KlockStore.business.service.item.ItemService;
import com.online.KlockStore.business.service.item.utils.ItemValidator;
import com.online.KlockStore.model.entities.Item;
import com.online.KlockStore.model.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    public ItemServiceImpl(ItemRepository itemRepository, ItemValidator itemValidator) {
        this.itemRepository = itemRepository;
        this.itemValidator = itemValidator;
    }

    @Override
    public Item buscarPorId(Long id) {
        itemValidator.validarId(id);
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item não encontrado!"));
    }

    @Override
    public List<Item> associarItens(List<Item> itensPedido) {
        itemValidator.validarItensPedido(itensPedido);
        return itensPedido.stream()
                .map(this::buscarOuCriarItem)
                .collect(Collectors.toList());
    }

    private Item buscarOuCriarItem(Item item) {
        if (item == null || item.getId() == null) {
            throw new IllegalArgumentException("Item ou ID do item não pode ser nulo.");
        }
        Optional<Item> itemExistente = itemRepository.findById(item.getId());
        return itemExistente.orElseGet(() -> salvarItem(item));
    }

    @Override
    public List<Item> listarTodos() {
        return itemRepository.findAll();
    }

    @Override
    public Item salvarItem(Item item) {
        itemValidator.validarItem(item);
        return itemRepository.save(item);
    }

    @Override
    public Item atualizarItem(Long id, Item itemAtualizado) {
        itemValidator.validarId(id);
        Item itemExistente = buscarPorId(id);

        if (itemAtualizado.getNome() != null) {
            itemExistente.setNome(itemAtualizado.getNome());
        }

        return itemRepository.save(itemExistente);
    }

    @Override
    public void excluirItem(Long id) {
        itemValidator.validarId(id);
        Item item = buscarPorId(id);
        itemRepository.delete(item);
    }
}