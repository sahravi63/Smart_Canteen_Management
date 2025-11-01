package com.example.canteen.service;

import com.example.canteen.model.User;
import com.example.canteen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Get all vendors
    public List<User> getAllVendors() {
        return userRepository.findByRole(User.Role.VENDOR);
    }

    // ✅ Approve vendor
    public void approveVendor(Long id) {
        User vendor = userRepository.findById(id).orElse(null);
        if (vendor != null && vendor.getRole() == User.Role.VENDOR) {
            vendor.setEnabled(true);
            userRepository.save(vendor);
        }
    }

    // ✅ Disable user
    public void disableUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setEnabled(false);
            userRepository.save(user);
        }
    }

    // ✅ Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ✅ Count total users
    public long countAllUsers() {
        return userRepository.count();
    }

    // ✅ Count pending vendors
    public long countPendingVendors() {
        return userRepository.countByRoleAndEnabled(User.Role.VENDOR, false);
    }
}
