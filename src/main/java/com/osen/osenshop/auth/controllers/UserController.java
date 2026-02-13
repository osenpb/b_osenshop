package com.osen.osenshop.auth.controllers;


import com.osen.osenshop.auth.domain.services.UserService;

import com.osen.osenshop.auth.application.dtos.UserResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

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
