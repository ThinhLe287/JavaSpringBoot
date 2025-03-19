package com.vanlang.webbanhang.service;

import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.repository.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    public List<Product> getAllProducts(String keyword) {
        if (keyword != null) {
            return productRepository.findAll(keyword);
        }
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(@NonNull Product product) {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new IllegalStateException("Product with ID " + product.getId() + " does not exist."));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setCategory(product.getCategory());
        return productRepository.save(existingProduct);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    public long countProducts() {
        return productRepository.count();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    public List<Product> getNewestProducts(int limit) {
        return productRepository.findTopNewestProducts(limit);
    }

    public List<Product> getBestSellingProducts(int limit) {
        return productRepository.findTopBestSellingProducts(limit);
    }
}
