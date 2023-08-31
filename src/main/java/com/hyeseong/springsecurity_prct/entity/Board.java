package com.hyeseong.springsecurity_prct.entity;

import com.hyeseong.springsecurity_prct.entity.Enum.BoardState;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class Board extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String boardTitle;

    @Column(nullable = false)
    private String boardContent;

    @Column(nullable = false)
    private int boardView;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BoardState boardState;

    @JoinColumn(nullable = false, name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Board(String boardTitle, String boardContent, int boardView, BoardState boardState, User user){
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardView = boardView;
        this.boardState = boardState;
        this.user = user;
    }

}
