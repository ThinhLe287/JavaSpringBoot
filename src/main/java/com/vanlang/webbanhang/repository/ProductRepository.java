package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE CONCAT(p.name, p.id, p.category.name) LIKE %:keyword%")
    List<Product> findAll(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.category.name = :category")
    List<Product> findByCategoryName(@Param("category") String category);

    @Query(value = "SELECT * FROM products ORDER BY created_date DESC LIMIT :limit", nativeQuery = true)
    List<Product> findTopNewestProducts(@Param("limit") int limit);

    @Query(value = "SELECT * FROM products ORDER BY sales DESC LIMIT :limit", nativeQuery = true)
    List<Product> findTopBestSellingProducts(@Param("limit") int limit);
}
