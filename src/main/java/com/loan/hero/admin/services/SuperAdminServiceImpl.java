package com.loan.hero.admin.services;

import com.loan.hero.auth.user.data.models.Address;
import com.loan.hero.auth.user.data.models.Role;
import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.hero_utility.HeroUtilities;
import com.loan.hero.init_token.InitToken;
import com.loan.hero.init_token.service.InitTokenService;
import com.loan.hero.loan_officer.data.dto.request.InviteRequest;
import com.loan.hero.loan_officer.data.models.LoanOfficer;
import com.loan.hero.loan_officer.data.repositories.LoanOfficerRepository;
import com.loan.hero.notification.mail.MailService;
import com.loan.hero.notification.mail.dto.EmailRequest;
import com.loan.hero.notification.mail.dto.MailInfo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class SuperAdminServiceImpl {
    private final LoanOfficerRepository loanOfficerRepository;
    private final InitTokenService initTokenService;
    private final TemplateEngine templateEngine;
    private final MailService mailService;
    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    private void createSuperAdmin() {
        if (!superAdminRepository.existsByEmail("dean@gmail.com")) {
            superAdminRepository.save(
                    User.builder()
                            .firstName("Michael")
                            .lastName("Dean")
                            .email("dean@gmail.com")
                            .userImage("my image")
                            .address(new Address())
                            .enabled(true)
                          //  .roles(Collections.singleton(Role.SUPER_ADMIN))
                            .roles(Set.of(Role.SUPER_ADMIN, Role.ADMIN))
                            .password(passwordEncoder.encode("password"))
                            .build()
            );
        }
    }

    @PreDestroy
    private void deleteSuperAdmin() {
        superAdminRepository.findByEmail("dean@gmail.com")
                .ifPresent(superAdminRepository::delete);
    }

    public String inviteAdmin(InviteRequest request) {
        final User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .build();
        final LoanOfficer loanOfficer = LoanOfficer.builder()
                .user(user)
                .build();
        final LoanOfficer savedLoanOfficer = loanOfficerRepository.save(loanOfficer);
        savedLoanOfficer.setEmployeeId(employeeId(savedLoanOfficer));
        loanOfficerRepository.save(savedLoanOfficer);

        sendLoanOfficerInvite(request, savedLoanOfficer);
        return "Loan officer invite sent successfully";
    }

    private void sendLoanOfficerInvite(InviteRequest request, LoanOfficer savedLoanOfficer) {
        final String token = HeroUtilities.generateToken(10);

        final InitToken initToken = InitToken.builder()
                .token(token)
                .email(request.getEmail())
                .revoked(false)
                .build();
        initTokenService.saveToken(initToken);

        final String fullName = request.getFirstName() + " " + request.getLastName();
        final Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("id", savedLoanOfficer.getEmployeeId());
        context.setVariable("token", token);

        final String content = templateEngine.process("admin_invite", context);

        final EmailRequest emailRequest = EmailRequest.builder()
                .to(Collections.singletonList(new MailInfo(fullName, request.getEmail())))
                .subject("Welcome Aboard")
                .htmlContent(content)
                .build();
        mailService.sendMail(emailRequest);
    }

    private String employeeId(LoanOfficer loanOfficer) {
        final String firstLetters = String.format("%s%s",
                loanOfficer.getUser().getFirstName().charAt(0),
                loanOfficer.getUser().getLastName().charAt(0)
        );
        final String toUppercase = firstLetters.toUpperCase();
        final String loanOfficerId = String.valueOf(loanOfficer.getId());
        final String userId = String.valueOf(loanOfficer.getUser().getId());
        return String.format("%s-0%s-0%s", toUppercase, userId, loanOfficerId);
    }
}