package com.api.xpress.notification.interfaces;

import com.api.xpress.notification.dto.EmailRequest;

public interface MailService {

    void sendMail(EmailRequest emailRequest);
}
