package com.api.xpress.notification;

import com.api.xpress.notification.dto.EmailRequest;
import com.api.xpress.notification.interfaces.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("dev")
@RequiredArgsConstructor
public class TestMailImpl implements MailService {

    @Override
    public void sendMail(EmailRequest emailRequest) {
        log.info("Sending mail to {}", emailRequest.getTo());
    }
}
