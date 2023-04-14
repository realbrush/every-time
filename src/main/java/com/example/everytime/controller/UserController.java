package com.example.everytime.controller;

import com.example.everytime.dto.user.UserCreateRequestDto;
import com.example.everytime.dto.user.UserLoginRequestDto;
import com.example.everytime.dto.user.UserResponseDto;
import com.example.everytime.dto.user.UserUpdateRequestDto;
import com.example.everytime.response.DefaultRes;
import com.example.everytime.response.ResponseMessage;
import com.example.everytime.response.StatusCode;
import com.example.everytime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    final private Validator validator;
    final private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> userList(){
        List<UserResponseDto> users = userService.getUserList();
        if(!users.isEmpty()){
            return new ResponseEntity(DefaultRes.res(StatusCode.OK,ResponseMessage.REQUEST_SUCCESS,users),HttpStatus.OK);
        }else{
            return new ResponseEntity(DefaultRes.res(StatusCode.NO_CONTENT,ResponseMessage.REQUEST_SUCCESS,""),HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping("{uuid}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable("uuid") UUID uuid){
        UserResponseDto user = userService.getUserData(uuid);
        if (user == null){
            return new ResponseEntity(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_USER,""), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,ResponseMessage.READ_USER,user),HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity createUser(@RequestBody() @Valid UserCreateRequestDto item){
        Set<ConstraintViolation<UserCreateRequestDto>> violation = validator.validate(item);
        if (!violation.isEmpty()){
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST,ResponseMessage.BAD_REQEUST,""),HttpStatus.BAD_REQUEST);
        }
        UUID uuid = userService.createUser(item);
        return new ResponseEntity(DefaultRes.res(StatusCode.CREATED,ResponseMessage.CREATED_USER,uuid),HttpStatus.CREATED);
    }


    @DeleteMapping("{uuid}")
    public ResponseEntity deleteUser(@PathVariable UUID uuid){
        Boolean isDeleted = userService.deleteUser(uuid);
        if(isDeleted){
            return new ResponseEntity(DefaultRes.res(StatusCode.OK,ResponseMessage.REQUEST_SUCCESS,uuid),HttpStatus.OK);
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR,ResponseMessage.REQUEST_FAIL,uuid),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping("{uuid}")
    public ResponseEntity updateUser(@RequestBody() @Valid UserUpdateRequestDto item,@PathVariable UUID uuid){
        UserResponseDto userResponseDto = userService.updateUser(uuid,item);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,ResponseMessage.REQUEST_SUCCESS,userResponseDto),HttpStatus.OK);
    }



    @PostMapping("/register")
    public ResponseEntity register(@RequestBody() @Valid UserCreateRequestDto userCreateRequestDto){
        //데이터 유효성 검사
        Set<ConstraintViolation<UserCreateRequestDto>> violations = validator.validate(userCreateRequestDto);
        if(!violations.isEmpty()){
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST,ResponseMessage.BAD_REQEUST,""),HttpStatus.BAD_REQUEST);
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


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody() @Valid UserLoginRequestDto userLoginRequestDto, HttpSession session){
        //로그인 데이터 유효성 검증
        Set<ConstraintViolation<UserLoginRequestDto>> violations = validator.validate(userLoginRequestDto);
        if(!violations.isEmpty()){
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST,ResponseMessage.BAD_REQEUST,""),HttpStatus.BAD_REQUEST);
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
