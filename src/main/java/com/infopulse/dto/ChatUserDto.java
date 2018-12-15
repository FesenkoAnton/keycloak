package com.infopulse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserDto {

    private String name;

    private String login;

    private String password;

    private Boolean isBanned;
}
