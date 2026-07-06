package com.api.xpress.notification;

import com.api.xpress.exceptions.XpressException;
import com.api.xpress.notification.dto.EmailRequest;
import com.api.xpress.notification.dto.MailInfo;
import com.api.xpress.notification.interfaces.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@Service
@Profile("dev")
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

    private final WebClient webClient;

//
//    public BrevoMailImpl(WebClient.Builder webClientBuilder) {
//        log.info("URL: {}, API: {}", mailUrl, apiKey);
//        this.webClient = webClientBuilder
//                .baseUrl(mailUrl)
//                .defaultHeader("api-key", apiKey)
//                .defaultHeader("accept", "application/json")
//                .defaultHeader("content-type", "application/json")
//                .build();
//    }

    @Override
    public void sendMail(EmailRequest emailRequest) {
        EmailRequest requestWithSender = buildRequestWithSender(emailRequest);

        this.webClient.post()
                .bodyValue(requestWithSender)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response ->
                        log.info("Email sent successfully to: {}", emailRequest.getTo()))
                .doOnError(error ->
                        log.error("Failed to send email to {}: {}",
                                emailRequest.getTo(), error.getMessage()))
                .onErrorResume(error -> {
                    throw new XpressException("Error sending email: " + error.getMessage());
                })
                .subscribe();
    }

    private EmailRequest buildRequestWithSender(EmailRequest emailRequest) {
        emailRequest.setSender(
                MailInfo.builder()
                        .name(appName)
                        .email(appEmail)
                        .build()
        );
        log.info("App name: {}, App email: {}", appName, appEmail);
        return emailRequest;
    }
}
