package com.example.everytime.service;

import com.example.everytime.domain.posts.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.everytime.domain.posts.PostRepository;
import com.example.everytime.dto.PostResponseDto;
import com.example.everytime.dto.PostSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<PostResponseDto> getPostList() {
        /**
            게시글 전체를 가져오는 메서드
            @return List<PostResponseDto>
         */
        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> response = new ArrayList<>();

        if(!posts.isEmpty()){
            response = posts.stream().map(
                    item -> new PostResponseDto(item)
            ).collect(Collectors.toList());
        }

        return response;
    }

    @Override
    public List<PostResponseDto> getPost(String title) {
        /**
            title 이 일치하는 게시글을 가져오는 메서드
            @param String title
            @return List<PostResponseDto>
         */
        List<Post> posts = postRepository.findByTitle(title);
        List<PostResponseDto> response = new ArrayList<>();

        if(!posts.isEmpty()){
            //검색결과가 있을 경우
            response = posts.stream().map(
                    item -> new PostResponseDto(item)
            ).collect(Collectors.toList());
        }

        return response;
    }

    @Override
    public Optional<PostResponseDto> getOnePost(UUID uuid){
        /**
         uuid가 일치하는 게시글을 가져오는 메서드
         @param uuid
         @return PostResponseDto
         */
        List<Post> posts = postRepository.findByUuid(uuid); //중복은 없음
        Optional<PostResponseDto> response = null;
        if(posts.size() == 1){
            response = Optional.of(new PostResponseDto(posts.get(0)));
        }
        return response;
    }


    @Override
    @Transactional
    public UUID createPost(PostSaveRequestDto item) {
        /**
            새로운 게시글을 만드는 메서드
            @param PostSaveRequestDto
            @return UUID (생성한 문서의 UUID)
         */

        /*
        * @todo 게시글 생성 성공,실패 로직 작성하기
        * @body 성공일 경우 성공 여부,실패일 경우 실패했다는 에러메세지를 리턴해야함
        * */

        return postRepository.save(item.toEntity()).getUuid();
    }

    @Override
    @Transactional
    public Optional<PostResponseDto> updatePost(Post item) {
        /**
         * 게시글을 업데이트 하는 메서드
         * @param PostUpdateRequestDto
         * @return
         * */

        /*
         * @todo dto update dto 형식에 맞게 구성하기
         * @body dto 형식에 맞게 다시짜고 업데이트 해주기
         */
        List<Post> posts = postRepository.findByUuid(item.getUuid()); //중복은 없음
        Optional<PostResponseDto> response = null;
        if(posts.size() == 1){
            response = Optional.of(new PostResponseDto(posts.get(0)));
        }
        return response;
    }

    @Override
    @Transactional
    public void deletePost(UUID uuid) {
        /*
        *  @todo 게시글 삭제 로직 구현하기
        *  @body post 삭제여부 확인한뒤, 삭제 할 게시글이 없으면 에러메세지를 출력하는 로직 작성하기
        * */
        postRepository.deleteByUuid(uuid);
    }

}

