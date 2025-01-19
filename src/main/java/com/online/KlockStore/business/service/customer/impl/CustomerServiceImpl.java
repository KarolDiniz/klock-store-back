package com.online.KlockStore.business.service.customer.impl;

import com.online.KlockStore.business.exception.customer.CustomerNotFoundException;
import com.online.KlockStore.business.exception.customer.InvalidCustomerException;
import com.online.KlockStore.business.service.customer.CustomerService;
import com.online.KlockStore.model.entities.Customer;
import com.online.KlockStore.model.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository clienteRepository;

    public CustomerServiceImpl(CustomerRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Customer buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID não pode ser nulo ou negativo.");
        }
        return clienteRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente com ID " + id + " não encontrado!"));
    }

    @Override
    public List<Customer> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Customer salvarCliente(@Valid Customer cliente) {
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new InvalidCustomerException("Email do cliente não pode ser vazio.");
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public void excluirCliente(@NotNull Long id) {
        Customer cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }
}
