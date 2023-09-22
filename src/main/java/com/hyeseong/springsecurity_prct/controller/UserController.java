package com.hyeseong.springsecurity_prct.controller;

import com.hyeseong.springsecurity_prct.dto.UserDto;
import com.hyeseong.springsecurity_prct.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Long signup(@RequestBody UserDto.Request userDto){
        return userService.SignUp(userDto);
    }
    @CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization")
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    public ResponseEntity<UserDto.Response> getMyUser(){
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

}
