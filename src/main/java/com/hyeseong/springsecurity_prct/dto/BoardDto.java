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
        //TODO: 여기엔 User 가 와야하는가, UserDto 가 와야하는가?
        private User user;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response{
        private String boardTitle;
        private String boardContent;
        private int boardView;
        private User user;
    }
}
