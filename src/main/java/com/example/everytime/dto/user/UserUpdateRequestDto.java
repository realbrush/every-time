package com.example.everytime.dto.user;

import com.example.everytime.domain.users.User;

import javax.validation.constraints.NotNull;

public class UserUpdateRequestDto {
    @NotNull()
    String email;
    @NotNull()
    String password;
    @NotNull()
    String nickname;

    public User toEntity(){
        return User.builder().email(email).password(password).nickname(nickname).build();
    }
}
