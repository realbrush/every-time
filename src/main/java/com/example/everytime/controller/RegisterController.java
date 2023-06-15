package com.example.everytime.controller;

import com.example.everytime.dto.user.UserCreateRequestDto;
import com.example.everytime.dto.user.UserResponseDto;
import com.example.everytime.response.DefaultRes;
import com.example.everytime.response.ResponseMessage;
import com.example.everytime.response.StatusCode;
import com.example.everytime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Set;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("register")
public class RegisterController {

    final private Validator validator;
    final private UserService userService;

    @PostMapping("")
    public ResponseEntity register(@RequestBody() @Valid UserCreateRequestDto userCreateRequestDto){
        //데이터 유효성 검사
        Set<ConstraintViolation<UserCreateRequestDto>> violations = validator.validate(userCreateRequestDto);
        if(!violations.isEmpty()){
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.BAD_REQEUST,""), HttpStatus.BAD_REQUEST);
        }

        //데이터 중복 검사
        Boolean isDuplicate_email = userService.checkDuplicationEmail(userCreateRequestDto.getEmail());
        Boolean isDuplicate_nickanme = userService.checkDuplicationNickname(userCreateRequestDto.getNickname());

        if(isDuplicate_email){
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST,ResponseMessage.DUPLICATE_USER,"중복된 이메일입니다"),HttpStatus.BAD_REQUEST);
            //데이터가 중복되어있을 경우
        }

        if (isDuplicate_nickanme) {
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST,ResponseMessage.DUPLICATE_USER,"중복된 닉네임입니다"),HttpStatus.BAD_REQUEST);
        }

        //회원 데이터 생성 및 조회
        UUID uuid = userService.createUser(userCreateRequestDto);
        UserResponseDto userResponseDto = userService.getUserData(uuid);

        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED,ResponseMessage.REQUEST_SUCCESS,userResponseDto),HttpStatus.CREATED);
    }
}
