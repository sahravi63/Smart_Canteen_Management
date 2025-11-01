package com.example.canteen.repository;

import com.example.canteen.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategory(String category);
    List<MenuItem> findByVendorId(Long vendorId);

    // ✅ Add this
    long countByVendorId(Long vendorId);
}
