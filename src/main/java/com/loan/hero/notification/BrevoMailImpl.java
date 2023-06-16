package com.loan.hero.notification;

import com.loan.hero.notification.dto.EmailRequest;
import com.loan.hero.notification.dto.MailInfo;
import com.loan.hero.notification.interfaces.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class BrevoMailImpl implements MailService {
    @Value("${sendinblue.mail.api_key}")
    private String apiKey;

    @Value("${sendinblue.mail.url}")
    private String mailUrl;

    @Value("${app.name}")
    private String appName;

    @Value("${app.email}")
    private String appEmail;


    @Override
    public String sendMail(EmailRequest emailRequest) {
        emailRequest.setSender(
                new MailInfo(appName, appEmail)
        );

        return WebClient.builder()
                .baseUrl(mailUrl)
                .defaultHeader("api-key", apiKey)
                .build()
                .post()
                .bodyValue(emailRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
