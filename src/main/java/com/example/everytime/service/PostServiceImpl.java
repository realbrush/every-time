package com.example.everytime.service;

import com.example.everytime.domain.posts.Post;

import java.util.*;
import java.util.stream.Collectors;

import com.example.everytime.domain.posts.PostRepository;
import com.example.everytime.domain.users.User;
import com.example.everytime.domain.users.UserRepository;
import com.example.everytime.dto.post.PostCreateRequestDto;
import com.example.everytime.dto.post.PostResponseDto;
import com.example.everytime.dto.user.UserLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public List<PostResponseDto> getPostList() {
        /**
            게시글 전체를 가져오는 메서드
            @return List<PostResponseDto>
         */
        List<Post> posts = postRepository.findByIsDeleted(false);
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
        List<Post> posts = postRepository.findByTitleAndIsDeleted(title,false);
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
    public PostResponseDto getOnePost(UUID uuid){
        /**
         uuid가 일치하는 게시글을 가져오는 메서드
         @param uuid
         @return PostResponseDto
         */
        Post post = postRepository.findByUuid(uuid).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다 uuid="+uuid)); //중복은 없음
        PostResponseDto response = new PostResponseDto(post);
        return response;
    }


    @Override
    @Transactional
    public UUID createPost(PostCreateRequestDto item, UserLoginRequestDto loginRequest) {
        /**
            새로운 게시글을 만드는 메서드
            @param PostCreateRequestDto
            @return UUID (생성한 문서의 UUID)
         */

        try{

            User user = userRepository.findByEmail(loginRequest.getEmail()).get(0);
            Post post = item.toEntity();
            post.setWriter(user); // 유저 데이터 설정

            return postRepository.save(post).getUuid();
        }catch (DataAccessException e){
            throw new PostCreationException("게시글 생성에 실패하였습니다.");
        }

    }

    @Override
    @Transactional
    public PostResponseDto updatePost(UUID uuid,Post request) {
        /**
         * 게시글을 업데이트 하는 메서드
         * @param PostUpdateRequestDto
         * @return
         * */
        Post post = postRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. uuid=" + uuid));

        // title 또는 contents 중 적어도 하나가 null이 아닌 경우에만 업데이트를 진행함
        if (request.getTitle() != null || request.getContents() != null) {
            String title = Optional.ofNullable(request.getTitle()).orElse(post.getTitle());
            String contents = Optional.ofNullable(request.getContents()).orElse(post.getContents());
            post.update(title, contents);
        } else {
            throw new IllegalArgumentException("제목 또는 내용 중 적어도 하나는 반드시 입력되어야 합니다.");
        }

        return new PostResponseDto(postRepository.save(post));
    }

    @Override
    @Transactional
    public boolean deletePost(UUID uuid) {
        /*
        *  @todo 게시글 삭제 로직 구현하기
        *  @body post 삭제여부 확인한뒤, 삭제 할 게시글이 없으면 에러메세지를 출력하는 로직 작성하기
        * */
        Post post = postRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. uuid=" + uuid));

        if(!post.isDeleted()){
            post.delete(true);
        }

        return post.isDeleted();

    }

    @Override
    public Long likePost(UUID uuid,boolean islike) {
        Optional<Post> post = postRepository.findByUuid(uuid);
        post.get().updateLike(islike);
        long goods = postRepository.save(post.get()).getGoods();
        return goods;
    }

    public class PostCreationException extends RuntimeException {
        public PostCreationException(String message) {
            super(message);
        }
    }

    public class PostNotFoundException extends RuntimeException {
        public PostNotFoundException(String message) {
            super(message);
        }
    }

}



