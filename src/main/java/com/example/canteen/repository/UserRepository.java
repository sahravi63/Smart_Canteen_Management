package com.example.canteen.repository;

import com.example.canteen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // ✅ Find all users by role
    List<User> findByRole(User.Role role);

    // ✅ Count by role and enabled status
    long countByRoleAndEnabled(User.Role role, boolean enabled);
}
