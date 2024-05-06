package com.test.phoneshope.repository;

import com.test.phoneshope.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
