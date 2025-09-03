package com.example.taskflow.domain.auth.repository;

import com.example.taskflow.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Long> {
}
