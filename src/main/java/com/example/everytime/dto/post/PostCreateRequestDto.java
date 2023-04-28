package com.example.everytime.dto.post;

import com.example.everytime.domain.posts.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotEmpty(message = "title must not be empty")
    private String title;
    @NotEmpty(message = "contents must not be empty")
    private String contents;

    @Builder
    public PostCreateRequestDto(String title, String contents){
        this.title = title;
        this.contents = contents;
    }
    public Post toEntity(){
        return Post.builder()
                .title(title)
                .contents(contents)
                .build();
    }
}

