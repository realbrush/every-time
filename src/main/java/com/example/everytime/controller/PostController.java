package com.example.everytime.controller;

import com.example.everytime.dto.post.PostCreateRequestDto;
import com.example.everytime.dto.post.PostResponseDto;
import com.example.everytime.dto.post.PostUpdateRequestDto;
import com.example.everytime.dto.user.UserLoginRequestDto;
import com.example.everytime.response.DefaultRes;
import com.example.everytime.response.ResponseMessage;
import com.example.everytime.response.StatusCode;
import com.example.everytime.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
@RequestMapping("posts")
public class PostController {
    /* to-do
    error handling
     */

    final private PostService postService;
    final private Validator validator;

    @GetMapping("/all") //모든 리소스를 가져옵니다
    public ResponseEntity<List<PostResponseDto>> postList(){
        List<PostResponseDto> posts = postService.getPostList();
        if(!posts.isEmpty()){
            return ResponseEntity.ok(posts);
        }else{
            return ResponseEntity.noContent().build();
        }
    }
    // 가져올 리소스가 없을 경우 처리해야함


    @GetMapping("{uuid}") //uuid 기준으로 찾은 리소스 하나를 가져옵니다
    public ResponseEntity findPost(@PathVariable("uuid") UUID uuid ){

        PostResponseDto post = postService.getOnePost(uuid);
            if (post == null) {
                return new ResponseEntity(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_POST, ""), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.accepted().body(post);
    }
    // 원하는 데이터가 없을 경우 처리해야함

    @PostMapping() //게시글을 생성합니다
    public ResponseEntity createPost(@RequestBody() @Valid PostCreateRequestDto item,HttpServletRequest request){
        Set<ConstraintViolation<PostCreateRequestDto>> violation = validator.validate(item);

        HttpSession session = request.getSession();
        UserLoginRequestDto user = (UserLoginRequestDto) session.getAttribute("user");

        if(!violation.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.accepted().body(postService.createPost(item,user));
    }
    // 요청한 데이터의 유효성을 확인하고 그렇지 않으면 잘못되었다는 메세지를 보내야함

    @PatchMapping("{uuid}")//게시글을 수정합니다
    public ResponseEntity updatePost(@PathVariable UUID uuid , @RequestBody() @Valid PostUpdateRequestDto request, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return new ResponseEntity(DefaultRes.res(StatusCode.BAD_REQUEST,ResponseMessage.REQUEST_FAIL,bindingResult.getAllErrors()),HttpStatus.BAD_REQUEST);
        }
        PostResponseDto updatedPost = postService.updatePost(uuid, request.toEntity());
        if (updatedPost == null) {
            return new ResponseEntity(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_POST, ""), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_POST, updatedPost), HttpStatus.OK);
    }
    //요청한 데이터의 유효성을 확인하고 그렇지 않으면 잘못되었다는 메세지를 보내야함

    @DeleteMapping("{uuid}")//게시글을 삭제합니다
    public ResponseEntity deletePost(@PathVariable("uuid") UUID uuid){
            boolean is_deleted = postService.deletePost(uuid);
            if(is_deleted){
                return new ResponseEntity(DefaultRes.res(StatusCode.OK,ResponseMessage.DELETE_POST,""),HttpStatus.OK);
            }else{
                return new ResponseEntity(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR,ResponseMessage.DELETE_POST_FAILED,""),HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }
    //해당하는 데이터가 없을 경우 에러메세지를 처리해야한다


    //좋아요

    @PostMapping("{uuid}/like")
    public ResponseEntity likePost(@PathVariable("uuid") UUID uuid){
        
        long goods = postService.likePost(uuid,true);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,ResponseMessage.UPDATE_POST,goods),HttpStatus.OK);

    }

    @DeleteMapping("{uuid}/like")
    public ResponseEntity unlikePost(@PathVariable("uuid") UUID uuid){
        long goods = postService.likePost(uuid,false);
        return new ResponseEntity(DefaultRes.res(StatusCode.OK,ResponseMessage.UPDATE_POST,goods),HttpStatus.OK);
    }
}
