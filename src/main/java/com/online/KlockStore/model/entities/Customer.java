package com.online.KlockStore.model.entities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private boolean isVip;

    public Customer() {

    }
    public Customer(String email, boolean isVip) {
        this.email = email;
        this.isVip = isVip;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public boolean isVip() {
        return isVip;
    }

}

