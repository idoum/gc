package com.mesprojets.gc.repo;

import com.mesprojets.gc.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
  Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
