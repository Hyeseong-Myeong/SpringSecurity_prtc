package com.hyeseong.springsecurity_prct.controller;

import com.hyeseong.springsecurity_prct.dto.AuthDto;
import com.hyeseong.springsecurity_prct.dto.TokenDto;
import com.hyeseong.springsecurity_prct.security.JwtFilter;
import com.hyeseong.springsecurity_prct.security.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    @PostMapping("/authenticate")
    public ResponseEntity<?> authorize(@Valid @RequestBody AuthDto.loginDto loginDto, HttpServletResponse response){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getUserPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        //JWT를 쿠키에 저장하도록 하는 방법
//        Cookie cookie = new Cookie("jwt", jwt);
//        cookie.setMaxAge((int)EXPIRATION_TIME/1000);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        return ResponseEntity.ok(null);

//          JWT를 바디에 넣어서 전송하는 방법
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }



}
