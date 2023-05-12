package com.web.store.app.backend.brand.service;

import com.web.store.app.backend.brand.dto.BrandDTO;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    Optional<BrandDTO> save(BrandDTO brandDTO);

    void deleteById(Integer id);

    Optional<BrandDTO> findById(Integer id);

    List<BrandDTO> findAllByCategoryName(String categoryName);

    List<BrandDTO> findAllByCategoryId(Integer categoryId);
}
