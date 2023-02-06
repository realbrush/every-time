package com.example.everytime.dto;

import com.example.everytime.domain.posts.Post;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PostResponseDto {

    private UUID uuid;
    private String title;
    private String content;
    private int goods;
    private LocalDateTime upload_date;

    public PostResponseDto(Post entity){
        this.uuid = entity.getUuid();
        this.title = entity.getTitle();
        this.content = entity.getContents();
        this.goods = entity.getGoods();
        this.upload_date = entity.getUpload_date();

    }
}
