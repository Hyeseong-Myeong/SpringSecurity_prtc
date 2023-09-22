package com.hyeseong.springsecurity_prct.controller;

import com.hyeseong.springsecurity_prct.dto.BoardDto;
import com.hyeseong.springsecurity_prct.entity.User;
import com.hyeseong.springsecurity_prct.service.AuthService;
import com.hyeseong.springsecurity_prct.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;
    private final AuthService authService;
    @CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization")
    @PostMapping("/post")
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    public BoardDto.Response post(@RequestBody BoardDto.Request boardDto){
        return boardService.createBoard(boardDto, authService.getNowUser());
    }
    @CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    public ResponseEntity<BoardDto.Response> readBoard(@PathVariable("id") Long id){
        return ResponseEntity.ok(boardService.readBoard(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BoardDto.ListDto>> readBoardList(){
        return ResponseEntity.ok(boardService.readBoardList());
    }
    @CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    public ResponseEntity<BoardDto.Response> updateBoard(
            @PathVariable("id") Long id,
            @RequestBody BoardDto.Request dto
    ){
        User user = authService.getNowUser();
        return ResponseEntity.ok(boardService.updateBoard(id, dto, user));
    }
}
