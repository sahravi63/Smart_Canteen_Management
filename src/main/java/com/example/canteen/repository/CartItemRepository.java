package com.example.canteen.repository;

import com.example.canteen.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserIdAndMenuItemId(Long userId, Long menuItemId);
    List<CartItem> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
