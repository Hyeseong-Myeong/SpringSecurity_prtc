package com.hyeseong.springsecurity_prct.repository;

import com.hyeseong.springsecurity_prct.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUserId(String userId);
    Optional<User> findOneByUserEmail(String userEmail);
    Optional<User> findOneWithAuthoritiesByUserId(String loginId);
}
