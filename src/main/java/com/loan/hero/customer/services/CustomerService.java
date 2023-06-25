package com.loan.hero.customer.services;


import com.loan.hero.agreement.data.model.LoanAgreement;
import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.customer.data.dto.request.Decision;
import com.loan.hero.customer.data.dto.request.InitRequest;
import com.loan.hero.customer.data.dto.request.SignUpRequest;
import com.loan.hero.customer.data.dto.request.UpdateCustomerRequest;
import com.loan.hero.customer.data.models.enums.AgreementDecision;
import com.loan.hero.customer.data.dto.response.InitResponse;
import com.loan.hero.customer.data.models.Customer;
import com.loan.hero.loan.data.dto.request.LoanRequest;
import com.loan.hero.loan.data.dto.response.LoanDTO;
import com.loan.hero.loan.data.models.LoanStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CustomerService {

    InitResponse initAccess(InitRequest initRequest);

    AuthenticationToken register(SignUpRequest signUpRequest);

    Customer getCurrentCustomer();

    String uploadCustomerImage(MultipartFile image);

    LoanDTO apply(LoanRequest loanRequest);

    LoanStatus viewLoanStatus(Long loanId);

    Map<String, String> allLoansStatus();

    LoanAgreement viewAgreement(Long agreementId);

    Customer updateCustomerProfile(UpdateCustomerRequest request);

    AgreementDecision agreementDecision(Decision agreementDecision);
}