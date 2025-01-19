package com.online.KlockStore.presentation.controller;

import com.online.KlockStore.business.service.customer.CustomerService;
import com.online.KlockStore.model.entities.Customer;
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

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setEmail("Clientea@gmail.com");

        customer2 = new Customer();
        customer2.setId(2L);
        customer2.setEmail("Clienteb@gmail.com");
    }

    @Test
    void listarTodos_DeveRetornarListaDeClientes() {
        List<Customer> customers = Arrays.asList(customer1, customer2);
        when(customerService.listarTodos()).thenReturn(customers);

        List<Customer> result = customerController.listarTodos();

        assertEquals(2, result.size());
        assertTrue(result.contains(customer1));
        assertTrue(result.contains(customer2));
        verify(customerService, times(1)).listarTodos();
    }

    @Test
    void buscarPorId_QuandoIdExistir_DeveRetornarCliente() {
        when(customerService.buscarPorId(1L)).thenReturn(customer1);

        ResponseEntity<Customer> response = customerController.buscarPorId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Clientea@gmail.com", response.getBody().getEmail());
        verify(customerService, times(1)).buscarPorId(1L);
    }

    @Test
    void buscarPorId_QuandoIdNaoExistir_DeveRetornar404() {
        when(customerService.buscarPorId(3L)).thenThrow(new RuntimeException("Cliente não encontrado"));

        ResponseEntity<Customer> response = customerController.buscarPorId(3L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(customerService, times(1)).buscarPorId(3L);
    }

    @Test
    void criarCliente_DeveRetornarClienteCriado() {
        when(customerService.salvarCliente(customer1)).thenReturn(customer1);

        ResponseEntity<Customer> response = customerController.criarCliente(customer1);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Clientea@gmail.com", response.getBody().getEmail());
        verify(customerService, times(1)).salvarCliente(customer1);
    }

    @Test
    void atualizarCliente_QuandoIdExistir_DeveRetornarClienteAtualizado() {
        when(customerService.salvarCliente(customer2)).thenReturn(customer2);

        ResponseEntity<Customer> response = customerController.atualizarCliente(2L, customer2);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Clienteb@gmail.com", response.getBody().getEmail());
        verify(customerService, times(1)).salvarCliente(customer2);
    }

    @Test
    void atualizarCliente_QuandoIdNaoExistir_DeveRetornar404() {
        when(customerService.salvarCliente(customer2)).thenThrow(new RuntimeException("Cliente não encontrado"));

        ResponseEntity<Customer> response = customerController.atualizarCliente(2L, customer2);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(customerService, times(1)).salvarCliente(customer2);
    }

    @Test
    void excluirCliente_QuandoIdExistir_DeveRetornarNoContent() {
        doNothing().when(customerService).excluirCliente(1L);

        ResponseEntity<Void> response = customerController.excluirCliente(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(customerService, times(1)).excluirCliente(1L);
    }

    @Test
    void excluirCliente_QuandoIdNaoExistir_DeveRetornar404() {
        doThrow(new RuntimeException("Cliente não encontrado")).when(customerService).excluirCliente(3L);

        ResponseEntity<Void> response = customerController.excluirCliente(3L);

        assertEquals(404, response.getStatusCodeValue());
        verify(customerService, times(1)).excluirCliente(3L);
    }
}
