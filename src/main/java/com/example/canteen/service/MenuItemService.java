package com.example.canteen.service;

import com.example.canteen.model.MenuItem;
import com.example.canteen.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {
    private final MenuItemRepository repo;
    public MenuItemService(MenuItemRepository repo){ this.repo = repo; }

    public MenuItem save(MenuItem m){ return repo.save(m); }
    public Optional<MenuItem> findById(Long id){ return repo.findById(id); }
    public List<MenuItem> findAll(){ return repo.findAll(); }
    public void delete(Long id){ repo.deleteById(id); }
    public List<MenuItem> findByCategory(String category){ return repo.findByCategory(category); }

    // ✅ Add these methods
    public long countItems() {
        return repo.count();
    }

    public long countByVendor(Long vendorId) {
        return repo.countByVendorId(vendorId);
    }
}
