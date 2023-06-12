package com.loan.hero.notification;

import com.loan.hero.hero_utility.HeroUtilities;
import com.loan.hero.notification.dto.EmailRequest;
import com.loan.hero.notification.dto.MailInfo;
import com.loan.hero.notification.interfaces.InitTokenService;
import com.loan.hero.notification.interfaces.MailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InitTokenServiceImpl implements InitTokenService {
    private final InitTokenRepository initTokenRepository;
    private final TemplateEngine templateEngine;
    private final MailService mailService;

    @Override
    public void saveToken(InitToken initToken) {
        initTokenRepository.save(initToken);
    }

    @Override
    public String sendSignUpMail(String email) {
        String token = HeroUtilities.generateToken(7);
        initTokenRepository.save(
                InitToken.builder()
                        .token(token)
                        .email(email)
                        .build()
        );

        Context context = new Context();
        context.setVariables(
                Map.of(
                        "email", email,
                        "token", token
                )
        );
        String content = templateEngine.process("customer_mail", context);
        EmailRequest emailRequest = EmailRequest.builder()
                .to(List.of(new MailInfo(email, email)))
                .subject("Welcome to Hero Money")
                .htmlContent(content)
                .build();
        return mailService.sendMail(emailRequest);
    }

    @Override
    public boolean isValid(InitToken initToken) {
//        Optional<InitToken> initToken =
//                initTokenRepository.findValidByTokenAndEmail(token, email);
        return initToken != null &&
                initToken.getExpireAt()
                        .isAfter(LocalDateTime.now());


//        return initToken.isPresent() &&
//                initToken.get()
//                        .getExpireAt()
//                        .isAfter(LocalDateTime.now());
    }

    @Override
    public Optional<InitToken> findByTokenAndEmail(String token, String email) {
        return initTokenRepository.findValidByTokenAndEmail(token, email);
    }
}
