package com.elevateluxe.controller;

import com.elevateluxe.dto.ProductDto;
import com.elevateluxe.entity.Order;
import com.elevateluxe.repository.CategoryRepository;
import com.elevateluxe.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("totalProducts", productService.findAllForAdmin().size());
        model.addAttribute("totalUsers", userService.findAllUsers().size());
        model.addAttribute("totalOrders", orderService.findAll().size());
        model.addAttribute("recentOrders", orderService.findAll().stream().limit(5).toList());
        model.addAttribute("featuredProducts", productService.findFeatured());
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("currentPage", "products");
        model.addAttribute("products", productService.findAllForAdmin());
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("currentPage", "products");
        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "products");
        var product = productService.findById(id);
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setBrand(product.getBrand());
        dto.setFeatured(product.isFeatured());
        dto.setActive(product.isActive());
        dto.setImageUrl(product.getImageUrl());
        if (product.getCategory() != null) dto.setCategoryId(product.getCategory().getId());
        model.addAttribute("productDto", dto);
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@Valid @ModelAttribute("productDto") ProductDto dto,
                              BindingResult result,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("currentPage", "products");
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/product-form";
        }
        try {
            String imageUrl = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                imageUrl = fileStorageService.storeFile(imageFile);
            }
            productService.save(dto, imageUrl);
            redirectAttributes.addFlashAttribute("successMsg", "Product saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Error saving product: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("successMsg", "Product deactivated.");
        return "redirect:/admin/products";
    }

    @PostMapping("/products/stock/{id}")
    public String updateStock(@PathVariable Long id, @RequestParam int quantity,
                              RedirectAttributes redirectAttributes) {
        productService.updateStock(id, quantity);
        redirectAttributes.addFlashAttribute("successMsg", "Stock updated.");
        return "redirect:/admin/products";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("currentPage", "orders");
        model.addAttribute("orders", orderService.findAll());
        return "admin/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "orders");
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("statuses", Order.OrderStatus.values());
        return "admin/order-detail";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam Order.OrderStatus status,
                                    RedirectAttributes redirectAttributes) {
        orderService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("successMsg", "Order status updated to " + status);
        return "redirect:/admin/orders/" + id;
    }

    // === Users ===
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("currentPage", "users");
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.toggleUserStatus(id);
        redirectAttributes.addFlashAttribute("successMsg", "User status updated.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/promote")
    public String promoteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.promoteToAdmin(id);
        redirectAttributes.addFlashAttribute("successMsg", "User promoted to admin.");
        return "redirect:/admin/users";
    }
}
