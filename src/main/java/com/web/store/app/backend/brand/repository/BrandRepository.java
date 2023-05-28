package com.web.store.app.backend.brand.repository;

import com.web.store.app.backend.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

    List<Brand> findBrandsByCategory_Name(String name);
    List<Brand> findAllByCategory_Id(Integer id);
}
