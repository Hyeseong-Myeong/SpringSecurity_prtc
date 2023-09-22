package com.hyeseong.springsecurity_prct.service;

import com.hyeseong.springsecurity_prct.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String userEmail){
        return userRepository.findWithAuthoritiesByUserEmail(userEmail)
                .map(user -> createUser(userEmail, user))
                .orElseThrow(() -> new UsernameNotFoundException(userEmail + " DB에 존재하지 않는 유저"));
    }

    private User createUser(String loginId, com.hyeseong.springsecurity_prct.entity.User user){

        List<GrantedAuthority> grantedAuthorityList = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new User(user.getUserEmail(),
                user.getUserPassword(),
                grantedAuthorityList);
    }
}
