package com.mesprojets.gc.repo;

import com.mesprojets.gc.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
  Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
