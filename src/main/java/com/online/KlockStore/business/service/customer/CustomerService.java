package com.online.KlockStore.business.service.customer;

import com.online.KlockStore.model.entities.Customer;
import java.util.List;

public interface CustomerService {

    Customer buscarPorId(Long id);

    List<Customer> listarTodos();

    Customer salvarCliente(Customer cliente);

    Customer atualizarCliente(Long id, Customer clienteAtualizado);

    void excluirCliente(Long id);
}