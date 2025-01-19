package com.online.KlockStore.business.service.customer.impl;

import com.online.KlockStore.business.exception.customer.CustomerNotFoundException;
import com.online.KlockStore.model.entities.Customer;
import com.online.KlockStore.model.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository clienteRepository;

    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerServiceImpl(clienteRepository);
    }

    @Test
    void testBuscarPorId_ClienteExistente() {
        Long customerId = 1L;
        Customer customer = new Customer("cliente@exemplo.com", true);
        customer.setId(customerId);
        when(clienteRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));

        Customer resultado = customerService.buscarPorId(customerId);

        assertNotNull(resultado);
        assertEquals(customerId, resultado.getId());
        assertEquals("cliente@exemplo.com", resultado.getEmail());
    }

    @Test
    void testBuscarPorId_ClienteNaoExistente() {
        Long customerId = 1L;
        when(clienteRepository.findById(customerId)).thenReturn(java.util.Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> customerService.buscarPorId(customerId));
        assertEquals("Cliente com ID 1 não encontrado!", exception.getMessage());
    }

    @Test
    void testListarTodos() {
        Customer customer1 = new Customer("cliente1@exemplo.com", false);
        Customer customer2 = new Customer("cliente2@exemplo.com", true);
        when(clienteRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<Customer> customers = customerService.listarTodos();

        assertEquals(2, customers.size());
        assertTrue(customers.contains(customer1));
        assertTrue(customers.contains(customer2));
    }

    @Test
    void testSalvarCliente() {
        Customer customer = new Customer("cliente@exemplo.com", true);
        when(clienteRepository.save(customer)).thenReturn(customer);

        Customer clienteSalvo = customerService.salvarCliente(customer);

        assertNotNull(clienteSalvo);
        assertEquals(customer.getEmail(), clienteSalvo.getEmail());
        assertTrue(clienteSalvo.isVip());
    }

    @Test
    void testExcluirCliente() {
        Long customerId = 1L;
        Customer customer = new Customer("cliente@exemplo.com", true);
        customer.setId(customerId);
        when(clienteRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));

        customerService.excluirCliente(customerId);

        verify(clienteRepository, times(1)).delete(customer);
    }

    @Test
    void testExcluirCliente_ClienteNaoExistente() {
        Long customerId = 1L;
        when(clienteRepository.findById(customerId)).thenReturn(java.util.Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> customerService.excluirCliente(customerId));
        assertEquals("Cliente com ID 1 não encontrado!", exception.getMessage());
    }
}

