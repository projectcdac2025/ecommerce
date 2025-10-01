package com.yourapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.yourapp.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);//used for login/authentication
  boolean existsByEmail(String email);//check for duplicates during signup
}
