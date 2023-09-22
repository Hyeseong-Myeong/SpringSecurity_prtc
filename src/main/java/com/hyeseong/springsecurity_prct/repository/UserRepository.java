package com.hyeseong.springsecurity_prct.repository;

import com.hyeseong.springsecurity_prct.entity.Authority;
import com.hyeseong.springsecurity_prct.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findWithAuthoritiesByUserEmail(String Email);
    Optional<User> findByUserName(String userName);
}
