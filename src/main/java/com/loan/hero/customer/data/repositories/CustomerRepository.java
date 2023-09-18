package com.loan.hero.customer.data.repositories;


import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.customer.data.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUserEmail(String email);
    Optional<Customer> findByUser(User user);
}
