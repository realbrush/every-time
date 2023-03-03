package com.example.everytime.domain.posts;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "posts_table")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BINARY(16)",nullable = false, updatable = false)
    private UUID uuid;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String title;

    @Column(columnDefinition = "VARCHAR(5000)", nullable = false)
    private String contents;

    @Column(columnDefinition = "INT", nullable = false)
    private int goods;
    //좋아요

    @Column(columnDefinition = "BOOL",nullable = false,name = "is_deleted")
    private boolean isDeleted;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime upload_date;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime deleted_date;

    @Builder
    public Post(UUID uuid,String title,String contents, int goods) {
        this.uuid =uuid;
        this.title = title;
        this.contents = contents;
        this.goods = goods;
    }

    public void update(String title, String contents) {
        if(!title.isEmpty()){
            this.title = title;
        }
        if (!contents.isEmpty()){
            this.contents = contents;
        }

    }

    public void delete(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    @PrePersist
    public void prePersist(){
        this.uuid = UUID.randomUUID();
    }


}


