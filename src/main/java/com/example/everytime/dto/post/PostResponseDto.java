package com.example.everytime.dto.post;

import com.example.everytime.domain.posts.Post;
import com.example.everytime.domain.users.User;
import com.example.everytime.dto.user.UserResponseDto;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PostResponseDto {

    @NotEmpty(message = "post id must not be empty")
    private UUID uuid;

    @NotEmpty(message = "user must not be empty")
    private UserResponseDto user;

    @NotEmpty(message = "title must not be empty")
    private String title;

    @NotEmpty(message = "contents must not be empty")
    private String contents;

    private int goods;

    private LocalDateTime upload_date;

    public PostResponseDto(Post entity){
        this.uuid = entity.getUuid();
        this.title = entity.getTitle();
        this.user = new UserResponseDto(entity.getWriter());
        this.contents = entity.getContents();
        this.goods = entity.getGoods();
        this.upload_date = entity.getUpload_date();
    }

}
