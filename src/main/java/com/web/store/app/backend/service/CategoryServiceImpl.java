package com.web.store.app.backend.service;

import com.web.store.app.backend.entity.Category;
import com.web.store.app.backend.dto.CategoryDTO;
import com.web.store.app.backend.repository.sql.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public Optional<CategoryDTO> save(CategoryDTO categoryDTO) {

        return Optional.of(mapToCategoryDto(categoryRepository.save(mapToCategory(categoryDTO))));
    }

    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    public Optional<CategoryDTO> findById(Integer id) {

        return categoryRepository.findById(id)
                .map(CategoryServiceImpl::mapToCategoryDto);
    }

    public Optional<List<CategoryDTO>> findAll() {

        return Optional.of(categoryRepository.findAll().stream()
                .map(CategoryServiceImpl::mapToCategoryDto).toList());
    }

    private static Category mapToCategory(CategoryDTO categoryDTO) {
        return new Category(categoryDTO.id(), categoryDTO.name(), categoryDTO.parentId());
    }

    private static CategoryDTO mapToCategoryDto(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getParentId());
    }

}
