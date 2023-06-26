package com.example.everytime.dto.user;

import com.example.everytime.domain.users.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserCreateRequestDto {

    @NotEmpty(message= "email id must not be empty")
    @Email
    private String email;

    @NotEmpty(message = "nickname must not be empty")
    private String nickname;

    @NotEmpty(message = "password must not be empty")
    private String password;

    public UserCreateRequestDto(String email,String nickname,String password){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }


    public User toEntity(){
        return User.builder().email(email).nickname(nickname).password(password).build();
    }


}
