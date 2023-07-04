package com.example.everytime.service;

import com.example.everytime.domain.posts.Post;
import com.example.everytime.domain.users.User;
import com.example.everytime.domain.users.UserRepository;
import com.example.everytime.dto.post.PostDto;
import com.example.everytime.dto.post.PostResponseDto;
import com.example.everytime.dto.user.UserCreateRequestDto;
import com.example.everytime.dto.user.UserResponseDto;
import com.example.everytime.dto.user.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public List<UserResponseDto> getUserList() {
        List<User> userList = userRepository.findAll();
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for (User user : userList) {
            userResponseDtoList.add(new UserResponseDto(user));
        }
        return userResponseDtoList;
    }

    @Override
    public UserResponseDto getUserData(UUID uuid) {
        User user = userRepository.findByUuid(uuid);
        return new UserResponseDto(user);
    }


    @Override
    public UserResponseDto getUserData(String email) {
        List<User> userList = userRepository.findByEmail(email);
        User user = userList.get(0); // 유저는 무조건 하나 중복없음
        return new UserResponseDto(user);
    }

    @Override
    public UUID createUser(UserCreateRequestDto userCreateRequestDto) {
        User user = userCreateRequestDto.toEntity();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        UUID uuid = userRepository.save(user).getUuid();
        return uuid;
    }

    @Override
    public UserResponseDto updateUser(UUID uuid, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.save(userUpdateRequestDto.toEntity());
        UserResponseDto userResponseDto = new UserResponseDto(user);
        return userResponseDto;
    }

    @Override
    public Boolean deleteUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid);
        user.delete(true);
        userRepository.save(user);
        return user.isDeleted();
    }

    @Override
    public Boolean validationPassword(String email, String password) {
        User loginUser = userRepository.findByEmail(email).get(0);

        return passwordEncoder.matches(password, loginUser.getPassword());
    }

    @Override
    public Boolean checkDuplicationEmail(String email) {
        List<User> user = userRepository.findByEmail(email);

        //중복아님
        return !user.isEmpty();
        //중복임
    }

    @Override
    public Boolean checkDuplicationNickname(String nickname){
        List<User> user = userRepository.findByNickname(nickname);

        return !user.isEmpty();
    }

    @Override
    public Boolean existUser(String email){
        List<User> user = userRepository.findByEmail(email);
        return email.equals(user.get(0).getEmail());
    }

    @Override
    public List<PostDto> getPostByUser(UUID uuid) {
        List<Post> posts = userRepository.findByUuid(uuid).getPosts();
        List<PostDto> result = new ArrayList<>();
        //nested 된 dto에서 nested 되지 않은 dto로 변경함
        //기존 유저 정보가 들어가는 dto에서 유저정보를 빼고 post 정보만 들어간 dto로 변경함
        for ( Post p:posts ) {
            result.add(new PostDto(p));
        }
        return result;
    }

    @Override
    public UserResponseDto updateNickname(String email,String nickname) {
        User oldUser = userRepository.findByEmail(email).get(0);
        oldUser.setNickname(nickname);
        userRepository.save(oldUser);
        User newUser = userRepository.findByNickname(nickname).get(0);
        return new UserResponseDto(newUser);
    }

}
