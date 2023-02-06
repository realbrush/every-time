package com.example.everytime.service;

import com.example.everytime.domain.posts.Post;
import com.example.everytime.dto.PostCreateRequestDto;
import com.example.everytime.dto.PostResponseDto;
import com.example.everytime.dto.PostSaveRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface PostService {
    List<PostResponseDto> getPost(String title); // return title match post
    Optional<PostResponseDto> getOnePost(UUID uuid);
    List<PostResponseDto> getPostList(); // return all post
    UUID createPost(PostSaveRequestDto item); // return create post
    Optional<PostResponseDto> updatePost(Post post); // return updated post
    void deletePost(UUID uuid); // delete post by uuid

}



