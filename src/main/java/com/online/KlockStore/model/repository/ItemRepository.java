package com.online.KlockStore.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.online.KlockStore.model.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}

