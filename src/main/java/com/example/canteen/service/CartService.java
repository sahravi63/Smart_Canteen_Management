package com.example.canteen.service;

import com.example.canteen.model.CartItem;
import com.example.canteen.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository repo;
    public CartService(CartItemRepository repo) { this.repo = repo; }

    @Transactional
    public CartItem addItem(CartItem item) {
        System.out.println("Saving cart item for user=" + item.getUser().getId() +
                ", menu=" + item.getMenuItem().getId());

        Optional<CartItem> existing =
                repo.findByUserIdAndMenuItemId(item.getUser().getId(), item.getMenuItem().getId());

        CartItem result;
        if (existing.isPresent()) {
            CartItem ex = existing.get();
            ex.setQuantity(ex.getQuantity() + item.getQuantity());
            result = repo.save(ex);
            System.out.println("Updated existing cart item id=" + result.getId());
        } else {
            result = repo.save(item);
            System.out.println("Inserted new cart item id=" + result.getId());
        }
        return result;
    }

    public List<CartItem> listForUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public void remove(Long id) {
        repo.deleteById(id);
    }

    public void clearUserCart(Long userId) {
        repo.deleteByUserId(userId);
    }
}
