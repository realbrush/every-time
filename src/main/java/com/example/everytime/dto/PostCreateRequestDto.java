package com.example.everytime.dto;

import com.example.everytime.domain.posts.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

    private UUID uuid;
    private String title;
    private String content;

    public PostCreateRequestDto(String title, String content){
        this.title = title;
        this.content = content;
        this.uuid = UUID.randomUUID();
    }
    public Post toEntity(){
        return Post.builder()
                .uuid(UUID.randomUUID())
                .title(title)
                .contents(content)
                .build();
    }
}

