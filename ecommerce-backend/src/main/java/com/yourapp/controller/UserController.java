package com.yourapp.controller;

import com.yourapp.entity.User;
import com.yourapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create User (default role = USER)
    @PostMapping("/signup")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public User getUserByID(@PathVariable Long id) {
    	return userService.getUserById(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
    	 userService.deleteUser(id);
		 return ResponseEntity.ok("User deleted successfully!!!");
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,@RequestBody User updatedUser) {
    	return ResponseEntity.ok(userService.updateUser(id,updatedUser));
    }
}
