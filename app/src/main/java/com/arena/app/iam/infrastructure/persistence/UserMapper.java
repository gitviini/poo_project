package com.arena.app.iam.infrastructure.persistence;

import com.arena.app.iam.domain.model.User;

public class UserMapper {

    public static UserEntity toEntity(User domain) {
        return new UserEntity(
            domain.getId(),
            domain.getName(),
            domain.getUserId(),
            domain.getEmail(),
            domain.getPhone(),
            domain.getPassword(),
            domain.getRole(),
            domain.getBio(),
            domain.getImageBase64()
        );
    }

    public static User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setUserId(entity.getUserId());
        user.setEmail(entity.getEmail());
        user.setPhone(entity.getPhone());
        user.setPassword(entity.getPassword());
        user.setRole(entity.getRole());
        user.setBio(entity.getBio());
        user.setImageBase64(entity.getImageBase64());
        return user;
    }
}
