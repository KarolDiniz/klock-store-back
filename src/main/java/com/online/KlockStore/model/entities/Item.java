package com.online.KlockStore.model.entities;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do item é obrigatório.")
    private String nome;

    @Min(value = 0, message = "O preço do item deve ser maior ou igual a zero.")
    private double preco;

    @Min(value = 0, message = "A quantidade do item deve ser maior ou igual a zero.")
    private int quantidade;

    @Min(value = 0, message = "O estoque do item deve ser maior ou igual a zero.")
    private int estoque;

    public Item() {
    }

    public Item(String nome, double preco, int quantidade, int estoque) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoque = estoque;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
}

