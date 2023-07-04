package com.example.everytime.controller;

import com.example.everytime.dto.post.PostDto;
import com.example.everytime.dto.post.PostResponseDto;
import com.example.everytime.dto.user.UserCreateRequestDto;
import com.example.everytime.dto.user.UserLoginRequestDto;
import com.example.everytime.dto.user.UserResponseDto;
import com.example.everytime.dto.user.UserUpdateRequestDto;
import com.example.everytime.response.DefaultRes;
import com.example.everytime.response.ResponseMessage;
import com.example.everytime.response.StatusCode;
import com.example.everytime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


    //해당 유저의 게시글 조회
    @GetMapping("{uuid}/posts")
    public ResponseEntity getMyPost(@PathVariable UUID uuid){
        List<PostDto> posts = userService.getPostByUser(uuid);
        if(posts.isEmpty()){
            return new ResponseEntity(DefaultRes.res(StatusCode.NO_CONTENT,ResponseMessage.NO_POST,"작성한 게시글이 없습니다"),HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,ResponseMessage.REQUEST_SUCCESS,posts),HttpStatus.OK);
    }

    //해당 유저의 댓글 조회



    //닉네임 수정
    @PatchMapping ("{uuid}/nickname")
    public ResponseEntity updateNickname(String nickname, HttpServletRequest request){
        HttpSession session = request.getSession();
        UserLoginRequestDto user = (UserLoginRequestDto) session.getAttribute("user");
        UserResponseDto responseDto = userService.updateNickname(user.getEmail(),nickname);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,ResponseMessage.REQUEST_SUCCESS,responseDto),HttpStatus.OK);
    }
}
