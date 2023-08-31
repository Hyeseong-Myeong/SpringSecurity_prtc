package com.hyeseong.springsecurity_prct.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class AuthDto {
    @Data
    @AllArgsConstructor
    @Builder
    public static class loginDto{
        private String userId;
        private String userPassword;
    }
}
