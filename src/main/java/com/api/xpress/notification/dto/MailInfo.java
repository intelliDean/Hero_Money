package com.api.xpress.notification.dto;

import lombok.*;

@Builder
public record MailInfo(

        String name,

        String email
) {
}
