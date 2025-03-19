package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.Category;
import com.vanlang.webbanhang.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
