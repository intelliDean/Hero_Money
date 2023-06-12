package com.loan.hero.notification.interfaces;

import com.loan.hero.notification.dto.EmailRequest;

public interface MailService {
    String sendMail(EmailRequest emailRequest);
}
