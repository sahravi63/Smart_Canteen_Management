package com.example.canteen.repository;

import com.example.canteen.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 🔹 Orders belonging to a specific user
    List<Order> findByUserId(Long userId);

    // 🔹 Count how many orders a user has made
    long countByUserId(Long userId);

    // 🔹 Count how many orders belong to a vendor (for vendor dashboards)
    long countByVendorId(Long vendorId);

    // 🔹 Show 10 most recent orders (used in Admin dashboard)
    List<Order> findTop10ByOrderByCreatedAtDesc();

    // 🔹 Show 10 most recent orders for a specific vendor
    List<Order> findTop10ByVendorIdOrderByCreatedAtDesc(Long vendorId);
}
