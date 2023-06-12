package com.loan.hero.notification.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailInfo {

    private String name;

    private String email;
}
