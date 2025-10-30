package com.example.fixmate.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.fixmate.entities.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {
    Page<SubCategory> findByCategoryId(String categoryId, Pageable pageable);
}
