package com.web.store.app.backend.category.service;

import com.web.store.app.backend.category.dto.CategoryDTO;
import com.web.store.app.backend.category.entity.Category;
import com.web.store.app.backend.category.repository.CategoryRepository;
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

    @Override
    public Optional<CategoryDTO> save(CategoryDTO categoryDTO) {

        return Optional.of(mapToCategoryDto(categoryRepository.save(mapToCategory(categoryDTO))));
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<CategoryDTO> findById(Integer id) {

        return categoryRepository.findById(id)
                .map(this::mapToCategoryDto);
    }

    @Override
    public List<CategoryDTO> findAllByParentId(Integer parentId) {
        return categoryRepository.findAllByParentCategory_id(parentId).stream()
                .map(this::mapToCategoryDto).toList();
    }

    @Override
    public List<CategoryDTO> findAllByParentCategoryName(String parentCategoryName) {
       var subcategories = categoryRepository.findAllByParentCategory_Name(parentCategoryName).stream()
                .map(this::mapToCategoryDto).toList();
        return subcategories.isEmpty() ? null : subcategories;
    }

    @Override
    public Optional<List<CategoryDTO>> findAll() {

        return Optional.of(categoryRepository.findAll().stream()
                .map(this::mapToCategoryDto).toList());
    }

    @Override
    public List<CategoryDTO> findAllByParentIdIsNull() {
        return categoryRepository.findTopLevelCategories().stream()
                .map(this::mapToCategoryDto).toList();
    }

    private Category mapToCategory(CategoryDTO categoryDTO) {
        Category parentCategory = null;
        Optional<Category> getParentCategory = Optional.empty();
        if (categoryDTO.parentCategory() != null)
            getParentCategory = Optional.ofNullable(categoryRepository.findByName(categoryDTO.parentCategory()));
        if (getParentCategory.isPresent()) {
            parentCategory = getParentCategory.get();
        }
        return Category.builder().id(categoryDTO.id()).name(categoryDTO.name()).parentCategory(parentCategory).build();
    }

    private CategoryDTO mapToCategoryDto(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getParentCategory() == null ? null: category.getParentCategory().getName());
    }

}
