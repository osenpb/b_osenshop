package com.osen.osenshop.auth.infrastructure.controllers;


import com.osen.osenshop.auth.domain.services.UserService;

import com.osen.osenshop.auth.application.dtos.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(){
        List<UserResponse> userDtoList = userService.findAll();
        log.info("Obteniendo lista de usuarios...");
        return ResponseEntity.ok(userDtoList);
    }




}
