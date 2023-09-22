package com.hyeseong.springsecurity_prct.service;


import com.hyeseong.springsecurity_prct.entity.User;
import com.hyeseong.springsecurity_prct.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User getNowUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getPrincipal() == "anonimouseUser"){
            throw new SecurityException("로그인 되지 않음");
        }
        log.info("==getName : " + authentication.getName());
        log.info("==getPrincipal : " + authentication.getPrincipal());
        log.info("==get authorities : " + authentication.getAuthorities());
        log.info("==get Credenfial: " + authentication.getCredentials());

//getName: test1
//getPrincipal : org.springframework.security.core.userdetails.User [Username=test1, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, credentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_NORMAL]]

        User user = userRepository.findByUserEmail(authentication.getName()).orElseThrow();
        return user;
    }


}
