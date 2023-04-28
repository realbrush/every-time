package com.example.everytime.service;


import com.example.everytime.domain.users.User;
import com.example.everytime.dto.user.UserCreateRequestDto;
import com.example.everytime.dto.user.UserResponseDto;
import com.example.everytime.dto.user.UserUpdateRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    /*
    todo
    user crud service code
    login,register service code
     */

    List<UserResponseDto> getUserList();
    UserResponseDto getUserData(UUID uuid);
    UserResponseDto getUserData(String email);
    //crud 용 메서드

    UUID createUser(UserCreateRequestDto userCreateRequestDto);
    //유저 생성 메서드
    UserResponseDto updateUser(UUID uuid,UserUpdateRequestDto userUpdateRequestDto);
    //유저 업데이트

    Boolean deleteUser(UUID uuid);
    //유저 삭제

    Boolean checkDuplicationEmail(String email);
    Boolean checkDuplicationNickname(String nickname);
    Boolean validationPassword(String email,String password);

    Boolean existUser(String email);

}
