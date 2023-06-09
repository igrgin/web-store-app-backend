package com.web.store.app.backend.brand.service;

import com.web.store.app.backend.brand.dto.BrandDTO;
import com.web.store.app.backend.brand.entity.Brand;
import com.web.store.app.backend.brand.repository.BrandRepository;
import com.web.store.app.backend.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BrandServiceImpl implements BrandService {

    private CategoryRepository categoryRepository;
    private BrandRepository brandRepository;

    @Override
    public Optional<BrandDTO> save(BrandDTO brandDTO) {

        return Optional.of(mapToBrandDto(brandRepository.save(mapToBrand(brandDTO))));
    }

    @Override
    public void deleteById(Integer id) {
        brandRepository.deleteById(id);
    }

    @Override
    public Optional<BrandDTO> findById(Integer id) {

        return brandRepository.findById(id)
                .map(this::mapToBrandDto);
    }

    @Override
    public List<BrandDTO> findAllBrands() {
        return brandRepository.findAll().stream().map(this::mapToBrandDto).toList();
    }

    @Override
    public List<BrandDTO> findAllByParentCategoryName(String categoryName) {
        return brandRepository.findBrandsByCategory_Name(categoryName).stream().map(this::mapToBrandDto).toList();
    }

    @Override
    public List<BrandDTO> findAllByCategoryId(Integer categoryId) {
        return brandRepository.findAllByCategory_Id(categoryId).stream().map(this::mapToBrandDto).toList();
    }

    private Brand mapToBrand(BrandDTO brandDTO) {
        return new Brand(brandDTO.id(), brandDTO.name(), categoryRepository.findByName(brandDTO.category()));
    }

    private BrandDTO mapToBrandDto(Brand brand) {
        return new BrandDTO(brand.getId(), brand.getName(), brand.getCategory().getName());
    }

}
