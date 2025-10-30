package com.example.fixmate.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fixmate.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Page<Category> findAll(Pageable pageable);
}
