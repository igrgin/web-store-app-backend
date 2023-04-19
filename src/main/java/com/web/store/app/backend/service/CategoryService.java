package com.web.store.app.backend.service;

import com.web.store.app.backend.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<CategoryDTO> save(CategoryDTO categoryDTO);

    void deleteById(Integer id);

    Optional<CategoryDTO> findById(Integer id);

    Optional<List<CategoryDTO>> findAll();

}
