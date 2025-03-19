package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.service.CategoryService;
import com.vanlang.webbanhang.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService; // Đảm bảo bạn đã inject CategoryService

    // Display a list of all products
    @GetMapping
    public String showProducts(Model model,
                               @Param("keyword") String keyword) {
        model.addAttribute("products", productService.getAllProducts(keyword));
        model.addAttribute("keyword", keyword);
        return "/products/products-list";
    }

    // For adding a new product
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories()); // Load categories
        return "/products/add-product";
    }

    // Process the form for adding a new product
    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "products/add-product";
        }
        productService.addProduct(product);
        return "redirect:/products";
    }

    // For editing a product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories()); // Load categories
        return "/products/edit-product";
    }

    // Process the form for updating a product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            product.setId(id); // set id to keep it in the form in case of errors
            return "/products/update-product";
        }
        productService.updateProduct(product);
        return "redirect:/products";
    }

    // Handle request to delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public String getProductDetails(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "/products/product-details";
    }

    // Handle request to product statistics
    @GetMapping("/product-statistics")
    public String showProductStatistics(Model model) {
        model.addAttribute("products", productService.getAllProducts(null)); // Load all products for statistics
        return "/products/product-statistics";
    }

    @GetMapping("/category/{category}")
    public String showProductsByCategory(@PathVariable("category") String category, Model model) {
        model.addAttribute("products", productService.getProductsByCategory(category));
        model.addAttribute("title", category);
        return "/products/products-list";
    }

    @GetMapping("/newest")
    public String showNewestProducts(Model model) {
        model.addAttribute("products", productService.getNewestProducts(10)); // Lấy 10 sản phẩm mới nhất
        model.addAttribute("title", "Newest Products");
        return "/products/products-list";
    }

    @GetMapping("/best-selling")
    public String showBestSellingProducts(Model model) {
        model.addAttribute("products", productService.getBestSellingProducts(10)); // Lấy 10 sản phẩm bán chạy nhất
        model.addAttribute("title", "Best Selling Products");
        return "/products/products-list";
    }
}
