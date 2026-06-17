package com.arena.app.iam.domain.repository;

import com.arena.app.iam.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(String userId);
    Optional<User> findById(UUID id);
    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    User save(User user);
    void delete(User user);
}
