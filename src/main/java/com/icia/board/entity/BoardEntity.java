package com.icia.board.entity;

import com.icia.board.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(length = 20, nullable = false)
    public String boardWriter;
    @Column(length = 50, nullable = false)
    public String boardTitle;
    @Column(length = 20, nullable = false)
    public String boardPass;
    @Column(length = 500)
    public String boardContents;
    @Column()
    public int boardHits;
//    @CreationTimestamp
//    @Column(updatable = false)
//    private LocalDateTime createdAt;

    @Column
    private int fileAttached;

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    public static BoardEntity saveToBoardEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = setBoardEntityNoId(boardDTO);
        boardEntity.setBoardHits(0);

        return boardEntity;
    }

    public static BoardEntity saveToBoardEntityInId(BoardDTO boardDTO) {
        BoardEntity boardEntity = setBoardEntityNoId(boardDTO);
        boardEntity.setId(boardDTO.getId());
        return boardEntity;
    }

    private static BoardEntity setBoardEntityNoId(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        boardEntity.setFileAttached(0);
        return boardEntity;
    }

    public static BoardEntity SaveToEntityWithFile(BoardDTO boardDTO) {
        BoardEntity boardEntity = setBoardEntityNoId(boardDTO);
        boardEntity.setFileAttached(1);
        return boardEntity;

    }


}