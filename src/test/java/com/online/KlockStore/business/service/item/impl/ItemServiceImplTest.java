package com.online.KlockStore.business.service.item.impl;

import com.online.KlockStore.business.exception.item.ItemNotFoundException;
import com.online.KlockStore.model.entities.Item;
import com.online.KlockStore.model.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemService = new ItemServiceImpl(itemRepository);
    }


    @Test
    void testBuscarPorId_ItemNaoExistente() {
        Long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> itemService.buscarPorId(itemId));
        assertEquals("Item não encontrado!", exception.getMessage());
    }

    @Test
    void testAssociarItens_ItemNaoExistente() {
        Item itemInexistente = new Item("Relógio", 150.0, 10, 20);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> itemService.associarItens(List.of(itemInexistente)));
        assertEquals("Item não encontrado!", exception.getMessage());
    }

    @Test
    void testListarTodos() {
        Item item1 = new Item("Relógio", 150.0, 10, 20);
        Item item2 = new Item("Pulseira", 50.0, 5, 15);
        when(itemRepository.findAll()).thenReturn(List.of(item1, item2));

        List<Item> itens = itemService.listarTodos();

        assertEquals(2, itens.size());
        assertTrue(itens.contains(item1));
        assertTrue(itens.contains(item2));
    }

    @Test
    void testSalvarItem() {
        Item item = new Item("Relógio", 150.0, 10, 20);
        when(itemRepository.save(item)).thenReturn(item);

        Item itemSalvo = itemService.salvarItem(item);

        assertNotNull(itemSalvo);
        assertEquals(item.getId(), itemSalvo.getId());
        assertEquals(item.getNome(), itemSalvo.getNome());
    }

    @Test
    void testExcluirItem() {
        Long itemId = 1L;
        Item item = new Item("Relógio", 150.0, 10, 20);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        itemService.excluirItem(itemId);

        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    void testExcluirItem_ItemNaoExistente() {
        Long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> itemService.excluirItem(itemId));
        assertEquals("Item não encontrado!", exception.getMessage());
    }
}

