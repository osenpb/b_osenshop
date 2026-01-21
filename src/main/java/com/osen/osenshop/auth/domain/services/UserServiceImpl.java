package com.osen.osenshop.auth.domain.services;

import com.osen.osenshop.auth.application.mappers.AuthMapper;
import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.auth.domain.repository.UserRepository;
import com.osen.osenshop.auth.application.dtos.UserResponse;
import com.osen.osenshop.common.handler_exception.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserResponse> findAll() {

        List<User> userList = userRepository.findAll();

        return  userList.stream()
                    .map((AuthMapper::toDto))
                    .collect(Collectors.toList());
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User  not found with id: " + id));
    }
    @Override
    public UserResponse save(User user) {
        userRepository.save(user);
        return AuthMapper.toDto(user);
    }
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
