package com.loan.hero.notification;

import com.loan.hero.notification.dto.EmailRequest;
import com.loan.hero.notification.dto.MailInfo;
import com.loan.hero.notification.interfaces.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class BrevoMailImplTest {

    @Autowired
    private MailService mailService;

    @Test
    void sendMail() {
        final EmailRequest emailRequest = EmailRequest.builder()
                .subject("Welcome to Hero Money")
                .to(List.of(new MailInfo(
                        "Dean",
                        "o.michaeldean@gmail.com"
                )))
                .htmlContent("Message to send")
                .build();
        final String mailResponse = mailService.sendMail(
                emailRequest
        );
        assertThat(mailResponse).isNotNull();
    }
}