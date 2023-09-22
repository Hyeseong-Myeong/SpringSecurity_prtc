package com.hyeseong.springsecurity_prct.dto;

import com.hyeseong.springsecurity_prct.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class BoardDto {
    //innerclass로 DTO 생성

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Request{
        private String boardTitle;
        private String boardContent;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response{
        private String boardTitle;
        private String boardContent;
        private int boardView;
        private Long userId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ListDto{
        private String boardTitle;
        private Long userId;
    }
}
