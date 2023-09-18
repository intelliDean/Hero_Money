package com.loan.hero.notification.mail;

import com.loan.hero.notification.mail.dto.EmailRequest;

public interface MailService {
    String sendMail(EmailRequest emailRequest);
}
