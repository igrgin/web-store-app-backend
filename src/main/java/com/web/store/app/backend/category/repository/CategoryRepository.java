package com.web.store.app.backend.category.repository;

import com.web.store.app.backend.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAllByParentId(Integer parentId);

    List<Category> findAllByParentIdIsNull();
}
