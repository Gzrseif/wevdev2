package com.elevateluxe.service;

import com.elevateluxe.dto.ProductDto;
import com.elevateluxe.entity.Category;
import com.elevateluxe.entity.Product;
import com.elevateluxe.repository.CategoryRepository;
import com.elevateluxe.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> findAll() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> findAllForAdmin() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> findFeatured() {
        return productRepository.findByFeaturedTrueAndActiveTrue();
    }

    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    public List<Product> search(String query) {
        return productRepository.searchProducts(query);
    }

    @Transactional
    public Product save(ProductDto dto, String imageUrl) {
        Product product = dto.getId() != null ?
                productRepository.findById(dto.getId()).orElse(new Product()) : new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setBrand(dto.getBrand());
        product.setFeatured(dto.isFeatured());
        product.setActive(dto.isActive());

        if (imageUrl != null && !imageUrl.isEmpty()) {
            product.setImageUrl(imageUrl);
        } else if (dto.getImageUrl() != null) {
            product.setImageUrl(dto.getImageUrl());
        }

        if (dto.getCategoryId() != null) {
            Category cat = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(cat);
        }

        return productRepository.save(product);
    }

    @Transactional
    public void updateStock(Long productId, int quantity) {
        Product product = findById(productId);
        product.setStockQuantity(quantity);
        productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = findById(id);
        product.setActive(false);
        productRepository.save(product);
    }
}
