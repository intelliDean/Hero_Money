package com.api.xpress.customer.services;


import com.api.xpress.agreement.data.model.LoanAgreement;
import com.api.xpress.auth.security.user.AuthenticatedUser;
import com.api.xpress.auth.security.utility.AuthenticationToken;
import com.api.xpress.customer.data.dto.request.Decision;
import com.api.xpress.customer.data.dto.request.InitRequest;
import com.api.xpress.customer.data.dto.request.SignUpRequest;
import com.api.xpress.customer.data.dto.request.UpdateCustomerRequest;
import com.api.xpress.customer.data.models.enums.AgreementDecision;
import com.api.xpress.customer.data.dto.response.InitResponse;
import com.api.xpress.customer.data.models.Customer;
import com.api.xpress.loan.data.dto.request.LoanRequest;
import com.api.xpress.loan.data.dto.response.LoanDTO;
import com.api.xpress.loan.data.models.LoanStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CustomerService {

    InitResponse initAccess(InitRequest initRequest);

    AuthenticationToken register(SignUpRequest signUpRequest);

    Customer getCurrentCustomer(AuthenticatedUser currentUser);

    String uploadCustomerImage(MultipartFile image, AuthenticatedUser currentUser);

    LoanDTO apply(LoanRequest loanRequest, AuthenticatedUser currentUser);

    LoanStatus viewLoanStatus(Long loanId, AuthenticatedUser currentUser);

    Map<String, String> allLoansStatus(AuthenticatedUser currentUser);

    LoanAgreement viewAgreement(Long agreementId, AuthenticatedUser currentUser);

    Customer updateCustomerProfile(UpdateCustomerRequest request, AuthenticatedUser currentUser);

    AgreementDecision agreementDecision(Decision agreementDecision, AuthenticatedUser currentUser);
}