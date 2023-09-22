package com.hyeseong.springsecurity_prct.service;

import com.hyeseong.springsecurity_prct.dto.BoardDto;
import com.hyeseong.springsecurity_prct.entity.User;

import java.util.List;

public interface BoardService {
    public BoardDto.Response createBoard(BoardDto.Request boardDto, User user);

    public BoardDto.Response readBoard(Long boardId);
    public List<BoardDto.ListDto> readBoardList();

    public BoardDto.Response updateBoard(Long boardId,BoardDto.Request dto, User user);
}
