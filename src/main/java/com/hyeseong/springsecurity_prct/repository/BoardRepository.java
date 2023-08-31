package com.hyeseong.springsecurity_prct.repository;

import com.hyeseong.springsecurity_prct.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository <Board, Long> {
}
