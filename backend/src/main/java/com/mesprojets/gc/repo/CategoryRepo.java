package com.mesprojets.gc.repo;

import com.mesprojets.gc.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
