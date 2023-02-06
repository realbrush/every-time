package com.example.everytime.controller;

import com.example.everytime.dto.PostResponseDto;
import com.example.everytime.dto.PostSaveRequestDto;
import com.example.everytime.dto.RequestUpdatePostDto;
import com.example.everytime.domain.posts.Post;
import com.example.everytime.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts")
public class PostController {
    /* to-do
    error handling
     */

    final private PostService postService;

    @GetMapping("/all") //모든 리소스를 가져옵니다
    public ResponseEntity postList(){
        List<PostResponseDto> posts = postService.getPostList();
        return ResponseEntity.ok().body(posts);
    }
    //가져올 리소스가 없을 경우 처리해야함

    @GetMapping() //uuid 기준으로 찾은 리소스 하나를 가져옵니다
    public ResponseEntity findPost(@RequestParam("uuid") UUID uuid ){
        return ResponseEntity.accepted().body(postService.getOnePost(uuid));
    }
    //원하는 데이터가 없을 경우 처리해야함

    @PostMapping() //게시글을 생성합니다
    public UUID createPost(@RequestBody() PostSaveRequestDto item){
        return postService.createPost(item);
    }
    //요청한 데이터의 유효성을 확인하고 그렇지 않으면 잘못되었다는 메세지를 보내야함

    @PatchMapping()//게시글을 수정합니다
    public ResponseEntity updatePost(@RequestBody()RequestUpdatePostDto item){
        Post new_post = item.toEntity();
        return ResponseEntity.accepted().body(postService.updatePost(new_post));
    }
    //요청한 데이터의 유효성을 확인하고 그렇지 않으면 잘못되었다는 메세지를 보내야함

    @DeleteMapping()//게시글을 삭제합니다
    public ResponseEntity deletePost(@RequestParam("uuid") UUID uuid){
        postService.deletePost(uuid);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
    //해당하는 데이터가 없을 경우 에러메세지를 처리해야한다

}
