package com.hyeseong.springsecurity_prct.repository;

import com.hyeseong.springsecurity_prct.entity.Authority;
import com.hyeseong.springsecurity_prct.entity.Enum.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByAuthorityName(UserRole authority);
}
