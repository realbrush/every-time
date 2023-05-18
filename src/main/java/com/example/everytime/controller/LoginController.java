package com.example.everytime.controller;

import com.example.everytime.dto.user.UserLoginRequestDto;
import com.example.everytime.response.DefaultRes;
import com.example.everytime.response.ResponseMessage;
import com.example.everytime.response.StatusCode;
import com.example.everytime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("login")
public class LoginController {

    final private Validator validator;
    final private UserService userService;

    @PostMapping("")
    public ResponseEntity login(@RequestBody() @Valid UserLoginRequestDto userLoginRequestDto, HttpSession session){
        //로그인 데이터 유효성 검증
        Set<ConstraintViolation<UserLoginRequestDto>> violations = validator.validate(userLoginRequestDto);

        if(!violations.isEmpty()){
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.BAD_REQEUST,""), HttpStatus.BAD_REQUEST);
        }
        //데이터가 있는지 확인하기

        Boolean isExist = userService.existUser(userLoginRequestDto.getEmail());

        if(!isExist){
            //존재하지 않는 유저로 로그인 요청 할 경우
            return new ResponseEntity(DefaultRes.res(StatusCode.NO_CONTENT,ResponseMessage.NO_USER,""),HttpStatus.NO_CONTENT);
        }

        //아이디 값과 비밀번호 값을 확인하기
        Boolean isValidator = userService.validationPassword(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword());

        if(!isValidator){
            return new ResponseEntity(DefaultRes.res(StatusCode.UNAUTHORIZED,ResponseMessage.WRONG_PASSWORD,""),HttpStatus.UNAUTHORIZED);
            //비밀번호가 잘못입력되었을 경우
        }

        session.setAttribute("user",userLoginRequestDto); //세션에 유저 데이터 담기

        HttpCookie cookie = new HttpCookie("sessionId",session.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,cookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(DefaultRes.res(StatusCode.OK,ResponseMessage.REQUEST_SUCCESS,"login success"));
        //로그인 완료 메세지 보냄

    }
}
