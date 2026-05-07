package com.elevateluxe.controller;

import com.elevateluxe.entity.Order;
import com.elevateluxe.entity.User;
import com.elevateluxe.service.OrderService;
import com.elevateluxe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String myOrders(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("orders", orderService.findByUser(user));
        return "user/orders";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        Order order = orderService.findById(id);
        User user = userService.findByEmail(userDetails.getUsername());

        // Security: only owner or admin can see
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!order.getUser().getId().equals(user.getId()) && !isAdmin) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "user/order-detail";
    }
}
