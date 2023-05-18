package com.example.everytime.user;

import com.example.everytime.domain.users.User;
import com.example.everytime.domain.users.UserRepository;
import com.example.everytime.dto.user.UserCreateRequestDto;
import com.example.everytime.dto.user.UserResponseDto;
import com.example.everytime.dto.user.UserUpdateRequestDto;
import com.example.everytime.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    /*
    * Repository 메서드 검증은 할 필요가 없음
    * Service 메서드 내부 코드들이 정상적으로 호출되는지 검증
    * Repository를 Mock 하여 Service 메서드를 검증
    * */


    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    //가짜 Mock 객체
    //Mock 객체가 어떻게 작동하는지 어떻게 동작해야하는지 명시적으로 작성해주어야 함

    @InjectMocks
    private UserServiceImpl userService;


    String email = "tttt@naver.com";
    String nickname = "nicknnnnam";
    String password = "passssword";


    UUID uuid = UUID.randomUUID();
    User user = new User(1L,uuid,email,password,nickname);

    @Test
    void 유저데이터_찾기_UUID(){

        //given

        given(userRepository.findByUuid(uuid)).willReturn(user);

        //when
        UserResponseDto userResponseDto = userService.getUserData(uuid);

        //then
        assertThat(userResponseDto.getUuid()).isEqualTo(user.getUuid());

    }

    @Test
    void 유저데이터_찾기_EMAIL(){

        //given
        UUID uuid = UUID.randomUUID();
        User user = new User(1L,uuid,email,password,nickname);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        given(userRepository.findByEmail(email)).willReturn(userList);

        //when
        UserResponseDto userResponseDto = userService.getUserData(email);

        //then
        assertThat(userResponseDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void 유저데이터_생성(){
        //given
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(email,nickname,password);

        given(passwordEncoder.encode(password)).willReturn("encoder_password");
        given(userRepository.save(any(User.class))).willReturn(user);

        //when
        UUID result = userService.createUser(userCreateRequestDto);

        //then
        assertThat(result).isEqualTo(user.getUuid());

    }


    @Test
    void 유저데이터_업데이트(){
        //given
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto(email,"신명나요",password);
        given(userRepository.save(any(User.class))).willReturn(userUpdateRequestDto.toEntity());

        //when
        UserResponseDto userResponseDto = userService.updateUser(uuid,userUpdateRequestDto);

        //then
        assertThat(userResponseDto.getNickname()).isEqualTo("신명나요");

    }

    @Test
    void 유저삭제(){
        //given
        given(userRepository.findByUuid(uuid)).willReturn(user);
        given(userRepository.save(any(User.class))).willReturn(user);

        //when
        Boolean isDeleted = userService.deleteUser(uuid);

        //then
        assertThat(isDeleted).isTrue();

    }



}
