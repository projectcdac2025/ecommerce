package com.yourapp.service;

import com.yourapp.entity.User;
import com.yourapp.entity.Role;
import com.yourapp.repository.UserRepository;
import com.yourapp.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Create a new user (signup)
    public User createUser(User user) {
        // Assign default role
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Initialize roles list if null
        if (user.getRoles() == null) {
            user.setRoles(new java.util.HashSet<>());
        }

        user.getRoles().add(role);

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // ✅ Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ✅ Update user details
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        // Re-encode only if password changed
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }
}
