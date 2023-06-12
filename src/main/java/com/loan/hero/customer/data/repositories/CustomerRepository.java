package com.loan.hero.customer.data.repositories;


import com.loan.hero.customer.data.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByUserEmail(String email);
}
