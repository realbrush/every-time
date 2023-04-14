package com.example.everytime.dto.user;

import com.example.everytime.domain.users.User;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Getter
public class UserResponseDto {
    @NotEmpty(message = "user id must not be empty")
    private UUID uuid;

    @NotEmpty(message= "email id must not be empty")
    @Email
    private String email;

    @NotEmpty(message = "nickname must not be empty")
    private String nickname;

    public User toEntity(){
        return User.builder().uuid(uuid).email(email).nickname(nickname).build();
    }

    public UserResponseDto(User user){
        this.uuid = user.getUuid();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
