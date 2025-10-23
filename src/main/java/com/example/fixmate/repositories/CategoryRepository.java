package com.example.fixmate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fixmate.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
