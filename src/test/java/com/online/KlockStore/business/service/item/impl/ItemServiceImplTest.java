package com.online.KlockStore.business.service.item.impl;

import com.online.KlockStore.business.exception.item.ItemNotFoundException;
import com.online.KlockStore.business.service.item.utils.ItemValidator;
import com.online.KlockStore.model.entities.Item;
import com.online.KlockStore.model.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemValidator itemValidator;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        item = new Item();
        item.setId(1L);
        item.setNome("Item de Teste");
    }

    @Test
    void testBuscarPorId_ItemExistente() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item result = itemService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        verify(itemRepository).findById(1L);
    }

    @Test
    void testBuscarPorId_ItemNaoEncontrado() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            itemService.buscarPorId(1L);
        });

        assertEquals("Item não encontrado!", exception.getMessage());
    }

    @Test
    void testAssociarItens_ListaVazia() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.associarItens(Arrays.asList());
        });

        assertEquals("A lista de itens do pedido não pode ser nula ou vazia.", exception.getMessage());
    }

    @Test
    void testAssociarItens_ItemNulo() {
        Item itemNulo = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemService.associarItens(Arrays.asList(itemNulo));
        });

        assertEquals("Item ou ID do item não pode ser nulo.", exception.getMessage());
    }

    @Test
    void testSalvarItem_ValidaItem() {
        when(itemRepository.save(item)).thenReturn(item);

        Item result = itemService.salvarItem(item);

        assertNotNull(result);
        verify(itemRepository).save(item);
    }

    @Test
    void testExcluirItem_Sucesso() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).delete(item);

        itemService.excluirItem(1L);

        verify(itemRepository).delete(item);
    }

    @Test
    void testExcluirItem_ItemNaoEncontrado() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            itemService.excluirItem(1L);
        });

        assertEquals("Item não encontrado!", exception.getMessage());
    }
}
