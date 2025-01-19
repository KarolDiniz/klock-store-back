package com.online.KlockStore.presentation.controller;

import com.online.KlockStore.model.entities.Customer;
import com.online.KlockStore.business.service.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class CustomerController {
    private final CustomerService clienteService;

    public CustomerController(CustomerService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Customer> listarTodos() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> buscarPorId(@PathVariable Long id) {
        try {
            Customer cliente = clienteService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Customer> criarCliente(@RequestBody Customer cliente) {
        Customer clienteCriado = clienteService.salvarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> atualizarCliente(@PathVariable Long id, @RequestBody Customer clienteAtualizado) {
        try {
            Customer cliente = clienteService.salvarCliente(clienteAtualizado);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        try {
            clienteService.excluirCliente(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

