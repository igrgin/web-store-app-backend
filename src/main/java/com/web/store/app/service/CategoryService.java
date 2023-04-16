package com.web.store.app.service;

import com.web.store.app.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<CategoryDTO> save(CategoryDTO categoryDTO);

    void deleteById(Integer id);

    Optional<CategoryDTO> findById(Integer id);

    Optional<List<CategoryDTO>> findAll();

}
