package com.online.KlockStore.presentation.controller;

import com.online.KlockStore.business.service.item.ItemService;
import com.online.KlockStore.model.entities.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        item1 = new Item();
        item1.setId(1L);
        item1.setNome("Relógio A");

        item2 = new Item();
        item2.setId(2L);
        item2.setNome("Relógio B");
    }

    @Test
    void listarTodos_DeveRetornarListaDeItens() {
        List<Item> itens = Arrays.asList(item1, item2);
        when(itemService.listarTodos()).thenReturn(itens);

        List<Item> result = itemController.listarTodos();

        assertEquals(2, result.size());
        assertTrue(result.contains(item1));
        assertTrue(result.contains(item2));
        verify(itemService, times(1)).listarTodos();
    }

    @Test
    void buscarPorId_QuandoIdExistir_DeveRetornarItem() {
        when(itemService.buscarPorId(1L)).thenReturn(item1);

        ResponseEntity<Item> response = itemController.buscarPorId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Relógio A", response.getBody().getNome());
        verify(itemService, times(1)).buscarPorId(1L);
    }

    @Test
    void buscarPorId_QuandoIdNaoExistir_DeveRetornar404() {
        when(itemService.buscarPorId(3L)).thenThrow(new RuntimeException("Item não encontrado"));

        ResponseEntity<Item> response = itemController.buscarPorId(3L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(itemService, times(1)).buscarPorId(3L);
    }

    @Test
    void criarItem_DeveRetornarItemCriado() {
        when(itemService.salvarItem(item1)).thenReturn(item1);

        ResponseEntity<Item> response = itemController.criarItem(item1);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Relógio A", response.getBody().getNome());
        verify(itemService, times(1)).salvarItem(item1);
    }

    @Test
    void atualizarItem_QuandoIdExistir_DeveRetornarItemAtualizado() {
        when(itemService.salvarItem(item2)).thenReturn(item2);

        ResponseEntity<Item> response = itemController.atualizarItem(2L, item2);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Relógio B", response.getBody().getNome());
        verify(itemService, times(1)).salvarItem(item2);
    }

    @Test
    void atualizarItem_QuandoIdNaoExistir_DeveRetornar404() {
        when(itemService.salvarItem(item2)).thenThrow(new RuntimeException("Item não encontrado"));

        ResponseEntity<Item> response = itemController.atualizarItem(2L, item2);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(itemService, times(1)).salvarItem(item2);
    }

    @Test
    void excluirItem_QuandoIdExistir_DeveRetornarNoContent() {
        doNothing().when(itemService).excluirItem(1L);

        ResponseEntity<Void> response = itemController.excluirItem(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(itemService, times(1)).excluirItem(1L);
    }

    @Test
    void excluirItem_QuandoIdNaoExistir_DeveRetornar404() {
        doThrow(new RuntimeException("Item não encontrado")).when(itemService).excluirItem(3L);

        ResponseEntity<Void> response = itemController.excluirItem(3L);

        assertEquals(404, response.getStatusCodeValue());
        verify(itemService, times(1)).excluirItem(3L);
    }
}
