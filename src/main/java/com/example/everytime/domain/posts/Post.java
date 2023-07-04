package com.example.everytime.domain.posts;

import com.example.everytime.domain.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JsonIgnore //순환참조관계 막기
    @JoinColumn(name="user_id",nullable = false)
    private User writer;

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
    public Post(UUID uuid,User writer,String title,String contents, int goods) {
        this.uuid = uuid;
        this.writer = writer;
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
    public void updateLike(boolean isLike){
        if(isLike){
            this.goods += 1;
        }else{
            this.goods -= 1;
        }
    }

    public void setWriter(User writer){
        this.writer = writer;
    }

    public void delete(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    @PrePersist
    public void prePersist(){
        this.uuid = UUID.randomUUID();
    }


}


