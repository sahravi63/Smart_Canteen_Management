package com.example.canteen.service;

import com.example.canteen.model.*;
import com.example.canteen.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final CartItemRepository cartRepo;

    public OrderService(OrderRepository orderRepo, OrderItemRepository orderItemRepo, CartItemRepository cartRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.cartRepo = cartRepo;
    }

    @Transactional
    public Order placeOrderFromCart(User user, List<CartItem> cartItems) {
        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setMenuItem(ci.getMenuItem());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getMenuItem().getPrice());
            oi.setSubtotal(ci.getMenuItem().getPrice().multiply(new BigDecimal(ci.getQuantity())));
            order.addItem(oi);
            total = total.add(oi.getSubtotal());
        }
        order.setTotal(total);
        Order saved = orderRepo.save(order);
        cartRepo.deleteByUserId(user.getId());
        return saved;
    }

    public List<Order> findByUser(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    public Order findById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    public Order save(Order o) {
        return orderRepo.save(o);
    }

    // ✅ Count helpers
    public long countOrders() {
        return orderRepo.count();
    }

    public long countOrdersByVendor(Long vendorId) {
        return orderRepo.countByVendorId(vendorId);
    }

    public long countOrdersByUser(Long userId) {
        return orderRepo.countByUserId(userId);
    }

    // ✅ NEW: Used in AdminController
    public List<Order> findRecentOrders() {
        return orderRepo.findTop10ByOrderByCreatedAtDesc();
    }

    // ✅ NEW: Used in DashboardController (Vendor)
    public List<Order> findRecentOrdersByVendor(Long vendorId) {
        return orderRepo.findTop10ByVendorIdOrderByCreatedAtDesc(vendorId);
    }
}
