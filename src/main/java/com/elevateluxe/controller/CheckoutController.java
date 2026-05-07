package com.elevateluxe.controller;

import com.elevateluxe.entity.Order;
import com.elevateluxe.entity.User;
import com.elevateluxe.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String checkout(HttpSession session, Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        if (cartService.getCartItems(session).isEmpty()) {
            return "redirect:/cart";
        }
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("cartItems", cartService.getCartItems(session));
        model.addAttribute("cartTotal", cartService.getCartTotal(session));
        model.addAttribute("user", user);
        return "shop/checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam String address,
                             @RequestParam String city,
                             @RequestParam String country,
                             @RequestParam String postalCode,
                             HttpSession session,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByEmail(userDetails.getUsername());
            Order order = orderService.placeOrder(user, cartService.getCartItems(session),
                    address, city, country, postalCode);
            cartService.clearCart(session);
            redirectAttributes.addFlashAttribute("successOrder", order.getOrderNumber());
            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/checkout";
        }
    }
}
