package com.loan.hero.customer.services;


import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.customer.data.dto.InitRequest;
import com.loan.hero.customer.data.dto.SignUpRequest;
import com.loan.hero.customer.data.dto.UpdateCustomerRequest;
import com.loan.hero.customer.data.dto.response.InitResponse;
import com.loan.hero.customer.data.models.Customer;
import com.loan.hero.loan.data.dto.request.LoanRequest;
import com.loan.hero.loan.data.dto.response.LoanDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {

    InitResponse initAccess(InitRequest initRequest);

    AuthenticationToken register(SignUpRequest signUpRequest);

    Customer getCurrentCustomer();

    String uploadCustomerImage(MultipartFile image);

    LoanDTO apply(LoanRequest loanRequest);

    Customer updateCustomerProfile(UpdateCustomerRequest request);
}