package com.elevateluxe.controller;

import com.elevateluxe.entity.Category;
import com.elevateluxe.entity.Product;
import com.elevateluxe.repository.CategoryRepository;
import com.elevateluxe.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featured", productService.findFeatured());
        model.addAttribute("categories", categoryRepository.findAll());
        return "shop/home";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) Long category,
                       @RequestParam(required = false) String q,
                       Model model) {
        List<Product> products;
        String pageTitle = "All Products";

        if (q != null && !q.isBlank()) {
            products = productService.search(q);
            pageTitle = "Search: " + q;
            model.addAttribute("searchQuery", q);
        } else if (category != null) {
            products = productService.findByCategory(category);
            categoryRepository.findById(category).ifPresent(cat -> {
                model.addAttribute("activeCategory", cat);
                model.addAttribute("pageTitle", cat.getName());
            });
        } else {
            products = productService.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("pageTitle", pageTitle);
        return "shop/catalog";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        // Related products
        List<Product> related = productService.findByCategory(product.getCategory().getId())
                .stream().filter(p -> !p.getId().equals(id)).limit(4).toList();
        model.addAttribute("related", related);
        return "shop/product-detail";
    }

    @GetMapping("/search")
    public String search(@RequestParam String q, Model model) {
        return "redirect:/shop?q=" + q;
    }

    @GetMapping("/category/{id}")
    public String category(@PathVariable Long id, Model model) {
        return "redirect:/shop?category=" + id;
    }
}
