package com.online.KlockStore.business.service.customer.impl;

import com.online.KlockStore.business.exception.customer.CustomerNotFoundException;
import com.online.KlockStore.business.service.customer.CustomerService;
import com.online.KlockStore.model.entities.Customer;
import com.online.KlockStore.model.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository clienteRepository;

    public CustomerServiceImpl(CustomerRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Customer buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente com ID " + id + " não encontrado!"));
    }

    @Override
    public List<Customer> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Customer salvarCliente(Customer cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void excluirCliente(Long id) {
        Customer cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }
}
