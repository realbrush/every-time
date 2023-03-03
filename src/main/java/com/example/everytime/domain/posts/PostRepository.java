package com.example.everytime.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByIsDeleted(boolean is_deleted);
    List<Post> findByTitle(String title);
    List<Post> findByTitleAndIsDeleted(String title,boolean is_deleted);
    Optional<Post> findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);
}
