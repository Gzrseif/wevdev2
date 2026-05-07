package com.elevateluxe.service;

import com.elevateluxe.entity.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CartService {
    private static final String CART_SESSION_KEY = "cart";

    @SuppressWarnings("unchecked")
    public Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addItem(HttpSession session, CartItem item) {
        Map<Long, CartItem> cart = getCart(session);
        if (cart.containsKey(item.getProductId())) {
            CartItem existing = cart.get(item.getProductId());
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
        } else {
            cart.put(item.getProductId(), item);
        }
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        Map<Long, CartItem> cart = getCart(session);
        if (quantity <= 0) {
            cart.remove(productId);
        } else if (cart.containsKey(productId)) {
            cart.get(productId).setQuantity(quantity);
        }
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeItem(HttpSession session, Long productId) {
        Map<Long, CartItem> cart = getCart(session);
        cart.remove(productId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public int getCartCount(HttpSession session) {
        return getCart(session).values().stream()
                .mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal getCartTotal(HttpSession session) {
        return getCart(session).values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<CartItem> getCartItems(HttpSession session) {
        return new ArrayList<>(getCart(session).values());
    }
}
