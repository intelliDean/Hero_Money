package com.api.xpress.customer.data.repositories;


import com.api.xpress.auth.user.data.models.User;
import com.api.xpress.customer.data.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByUserEmail(String email);

    Optional<Customer> findCustomerByUserEmail(String email);
}
