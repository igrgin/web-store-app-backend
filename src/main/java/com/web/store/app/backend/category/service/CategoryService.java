package com.web.store.app.backend.category.service;

import com.web.store.app.backend.category.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<CategoryDTO> save(CategoryDTO categoryDTO);

    void deleteById(Integer id);

    Optional<CategoryDTO> findById(Integer id);
    List<CategoryDTO> findAllByParentId(Integer id);

    List<CategoryDTO> findAllByParentCategoryName(String parentCategoryName);

    Optional<List<CategoryDTO>> findAll();

    List<CategoryDTO> findAllByParentIdIsNull();
}
