package com.osen.osenshop.auth.domain.services;

import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.auth.application.dtos.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserResponse> findAll();
    User findById(Long id);
    UserResponse save(User user);
    void deleteById(Long id);
    Optional<User> findByEmail(String email);

}
