package com.test.phoneshope.repository;

import com.test.phoneshope.models.GoodsInStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsInStockRepository extends JpaRepository<GoodsInStock, Long> {
}
