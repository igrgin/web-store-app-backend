package com.web.store.app.backend.category.repository;

import com.web.store.app.backend.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAllByParentCategory_id(Integer parentId);

    List<Category> findAllByParentCategory_Name(String name);
    @Query(value = "SELECT c.* FROM category c WHERE c.parent_id is null;",nativeQuery = true)
    List<Category> findTopLevelCategories();

    Category findByName(String name);
}
