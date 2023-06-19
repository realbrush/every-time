package com.example.everytime.service;

import com.example.everytime.domain.posts.Post;
import com.example.everytime.dto.post.PostCreateRequestDto;
import com.example.everytime.dto.post.PostResponseDto;
import com.example.everytime.dto.user.UserCreateRequestDto;
import com.example.everytime.dto.user.UserLoginRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PostService {
    List<PostResponseDto> getPost(String title); // return title match post
    PostResponseDto getOnePost(UUID uuid);
    List<PostResponseDto> getPostList(); // return all post
    UUID createPost(PostCreateRequestDto item, UserLoginRequestDto user); // return create post
    PostResponseDto updatePost(UUID uuid,Post post); // return updated post
    boolean deletePost(UUID uuid); // delete post by uuid
    Long likePost(UUID uuid,boolean islike); // 해당 게시글의 좋아요 갯수를 리턴
}



