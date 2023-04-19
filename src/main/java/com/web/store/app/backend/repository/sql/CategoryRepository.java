package com.web.store.app.backend.repository.sql;

import com.web.store.app.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
