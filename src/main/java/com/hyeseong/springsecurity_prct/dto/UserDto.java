package com.hyeseong.springsecurity_prct.dto;

import com.hyeseong.springsecurity_prct.entity.Enum.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class info{
        private String userName;
        private String userEmail;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Request{
        private String userId;
        private String userPassword;
        private String userName;
        private String userEmail;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response{
        private String userName;
        private String userEmail;
//        private UserRole userRole;
    }
}
