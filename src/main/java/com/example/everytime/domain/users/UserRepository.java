package com.example.everytime.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByEmail(String email);
    List<User> findByNickname(String nicknmae);
    User findByUuid(UUID uuid);

}
