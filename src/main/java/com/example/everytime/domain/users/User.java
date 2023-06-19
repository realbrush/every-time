package com.example.everytime.domain.users;


import com.example.everytime.domain.posts.Post;
import com.example.everytime.dto.post.PostUpdateRequestDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users_table")
@Getter
@Setter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "writer")
    List<Post> posts;

    @Column(columnDefinition = "BINARY(16)",nullable = false,updatable = false)
    private UUID uuid;

    @Column(columnDefinition = "VARCHAR(255)",nullable = false)
    private String email;

    @Column(columnDefinition = "VARCHAR(255)",nullable = false)
    private String password;

    @Column(columnDefinition = "VARCHAR(30)",nullable = false)
    private String nickname;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime created_date;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime deleted_date;

    @Column(columnDefinition = "BOOL",nullable = false,name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public User(Long id,UUID uuid,String email,String password,String nickname){
        this.id = id;
        this.uuid = uuid;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
    public void delete(boolean isDeleted){
        this.isDeleted = true;
    }

    public void update(){
        /*
        * update logic 짜기 !
        * */
    }

    @PrePersist
    public void prePersist(){
        this.uuid = UUID.randomUUID();
    }

}
