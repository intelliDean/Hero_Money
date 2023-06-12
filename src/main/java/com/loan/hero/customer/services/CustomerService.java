package com.loan.hero.customer.services;


import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.customer.data.dto.InitRequest;
import com.loan.hero.customer.data.dto.SignUpRequest;
import com.loan.hero.customer.data.dto.response.InitResponse;

public interface CustomerService {

    InitResponse initAccess (InitRequest initRequest);

    AuthenticationToken register (SignUpRequest signUpRequest);
}
