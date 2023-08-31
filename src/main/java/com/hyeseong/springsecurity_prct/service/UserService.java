package com.hyeseong.springsecurity_prct.service;

import com.hyeseong.springsecurity_prct.dto.UserDto;
import com.hyeseong.springsecurity_prct.entity.Authority;
import com.hyeseong.springsecurity_prct.entity.Enum.UserRole;
import com.hyeseong.springsecurity_prct.entity.User;
import com.hyeseong.springsecurity_prct.repository.AuthorityRepository;
import com.hyeseong.springsecurity_prct.repository.UserRepository;
import com.hyeseong.springsecurity_prct.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;


    //유저 생성
    @Transactional
    public Long SignUp(UserDto.Request userDto){
        //중복 Email check
        if(userRepository.findOneByUserEmail(userDto.getUserEmail()).isPresent()){
            //중복 Email이 있는 경우 예외처리
        }
        //중복 ID check
        if(userRepository.findOneByUserId(userDto.getUserId()).isPresent()){
            //중복 ID가 있는 경우 예외처리
        }
        UserRole AUTH = UserRole.ROLE_NORMAL;
        Authority authority;
        Optional<Authority> optionalAuthority = authorityRepository.findByAuthorityName(AUTH);

        if(optionalAuthority.isPresent()){
            authority = optionalAuthority.get();
        }else{
            authority = Authority.builder()
                    .authorityName(UserRole.ROLE_NORMAL)
                    .build();
        }

        User user = User.builder()
                .userId(userDto.getUserId())
                .userPassword(passwordEncoder.encode(userDto.getUserPassword()))
                .userName(userDto.getUserName())
                .userEmail(userDto.getUserEmail())
                .authorities(Collections.singleton(authority))
                .build();

        return userRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public UserDto.info getUserWithAuthorities(String loginId){
        Optional<User> optionalUser =  userRepository.findOneWithAuthoritiesByUserId(loginId);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            return  UserDto.info.builder()
                    .userName(user.getUserName())
                    .userEmail(user.getUserEmail())
                    .build();
        }
        else
            return null;
    }

    @Transactional
    public UserDto.Response getMyUserWithAuthorities(){
        Optional<User> optionaluser =  SecurityUtil.getCurrentLoginId()
                .flatMap(userRepository::findOneWithAuthoritiesByUserId);

        if(optionaluser.isPresent()){
            User user = optionaluser.get();
            return UserDto.Response.builder()
                    .userName(user.getUserName())
                    .userEmail(user.getUserEmail())
                    .build();
        }else
            return null;
    }




}
