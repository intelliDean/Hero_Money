package com.loan.hero.customer.data.repositories;


import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.customer.data.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUserEmail(String email);
    Optional<Customer> findByUser(User user);
}
