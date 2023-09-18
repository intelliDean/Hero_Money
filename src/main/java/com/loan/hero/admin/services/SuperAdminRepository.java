package com.loan.hero.admin.services;


import com.loan.hero.auth.user.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SuperAdminRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
}
