package com.hyeseong.springsecurity_prct.service;

import com.hyeseong.springsecurity_prct.dto.BoardDto;
import com.hyeseong.springsecurity_prct.entity.Board;
import com.hyeseong.springsecurity_prct.entity.Enum.BoardState;
import com.hyeseong.springsecurity_prct.entity.User;
import com.hyeseong.springsecurity_prct.repository.BoardRepository;
import com.hyeseong.springsecurity_prct.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class boardServiceImpl implements BoardService  {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public BoardDto.Response createBoard(BoardDto.Request boardDto, User user){


        Board board = Board.builder()
                .boardTitle(boardDto.getBoardTitle())
                .boardContent(boardDto.getBoardContent())
                .boardState(BoardState.NORMAL)
                .boardView(0)
                .user(user)
                .build();
        Long id = boardRepository.save(board).getId();
        return readBoard(id);
    }
    @Transactional
    public BoardDto.Response readBoard(Long boardId){
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if(optionalBoard.isPresent()){
            Board board = optionalBoard.get();
            return BoardDto.Response.builder()
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .boardView(board.getBoardView())
                    .userId(board.getUser().getId())
                    .build();
        }
        else
            throw new EntityNotFoundException("존재하지 않는 글 입니다.");
    }


    public List<BoardDto.ListDto> readBoardList(){
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto.ListDto> boardDtoList = new ArrayList<>();

        for(Board b : boardList){
            BoardDto.ListDto dto = BoardDto.ListDto.builder()
                    .boardTitle(b.getBoardTitle())
                    .userId(b.getUser().getId())
                    .build();

            boardDtoList.add(dto);
        }

        return boardDtoList;
    }

    @Transactional
    public BoardDto.Response updateBoard(Long boardId,BoardDto.Request dto, User user) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if(optionalBoard.isPresent()){
            Board board = optionalBoard.get();
            if(board.getUser() == user){
                board.updateBoard(dto.getBoardTitle(), dto.getBoardContent());
                return readBoard(boardId);
            } else{
                throw new SecurityException("권한이 없는 사용자");
            }
        } else{
            throw new EntityNotFoundException("존재하지 않는 게시물");
        }
    }


}
