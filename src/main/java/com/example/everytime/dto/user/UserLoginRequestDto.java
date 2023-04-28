package com.example.everytime.dto.user;

import com.example.everytime.domain.users.User;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class UserLoginRequestDto {
    @NotEmpty(message= "email id must not be empty")
    @Email
    private String email;


    @NotEmpty(message = "password must not be empty")
    private String password;

    public UserLoginRequestDto(String email,String nickname,String password){
        this.email = email;
        this.password = password;
    }

    public User toEntity(){
        return User.builder().email(email).password(password).build();
    }
}
