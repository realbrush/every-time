package com.example.everytime.repository;

import com.example.everytime.domain.posts.Post;
import com.example.everytime.domain.posts.PostRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @After
    public void cleanup(){
        postRepository.deleteAll();
    }

    @Test
    @Transactional
    //테스트 완료 이후 데이터베이스에 영향이 없도록 함
    //roll back
    public void 게시글_저장_불러오기(){
        //given
        UUID uuid = UUID.randomUUID();
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postRepository.save(Post.builder().uuid(uuid).title(title).contents(content).build());

        //when
        List<Post> postsList = postRepository.findAll();

        //then
        Post posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContents()).isEqualTo(content);
    }

    @Test
    @Transactional
    public void 게시글_만들고_삭제하기(){
        //given
        UUID uuid = UUID.randomUUID();
        String title = "테스트 게시글";
        String content = "테스트 본문";

        Post post = postRepository.save(Post.builder().uuid(uuid).title(title).contents(content).build());

        //when
        postRepository.deleteByUuid(post.getUuid());

        //then
        List<Post> posts = postRepository.findByUuid(post.getUuid());
        assertThat(posts.size()).isEqualTo(0);

    }


}
