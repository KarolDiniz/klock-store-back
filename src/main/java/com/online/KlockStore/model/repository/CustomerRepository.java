package com.online.KlockStore.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.online.KlockStore.model.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

