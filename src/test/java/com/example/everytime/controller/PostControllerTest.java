package com.example.everytime.controller;

import com.example.everytime.domain.posts.Post;
import com.example.everytime.domain.posts.PostRepository;
import com.example.everytime.dto.post.PostCreateRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @Test
    @Transactional
    public void Post_등록된다() throws Exception{
        //given
        String title = "title";
        String contents = "content";

        PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                .title(title)
                .contents(contents)
                .build();

        String url = "http://localhost:"+ port + "/api/v1/posts";

        //when
        ResponseEntity<UUID> responseEntity = restTemplate.postForEntity(url,requestDto, UUID.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContents()).isEqualTo(contents);
    }

}
