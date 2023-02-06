package com.example.everytime.dto;


import com.example.everytime.domain.posts.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private UUID uuid;
    private String title;
    private String contents;

    @Builder
    public PostSaveRequestDto(String title,String contents){
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.contents = contents;

    }

    public Post toEntity(){
        return Post.builder()
                .uuid(UUID.randomUUID())
                .title(title)
                .contents(contents)
                .build();
    }
}
