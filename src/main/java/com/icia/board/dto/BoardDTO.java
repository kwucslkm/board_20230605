package com.icia.board.dto;

import com.icia.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.attoparser.trace.MarkupTraceEvent;
import org.springframework.core.SpringVersion;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor

public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardTitle;
    private String boardPass;
    private String boardContents;
    private int boardHits;
    private LocalDateTime createdAt;


    public static BoardDTO toDTO(BoardEntity b) {
//        BoardDTO boardDTO = new BoardDTO();
//        boardDTO.setId(b.getId());
//        boardDTO.setBoardPass(b.getBoardPass());
//        boardDTO.setBoardWriter(b.getBoardWriter());
//        boardDTO.setBoardTitle(b.getBoardTitle());
//        boardDTO.setBoardContents(b.getBoardContents());
//        boardDTO.setBoardHits(b.getBoardHits());
//        return boardDTO;
        return BoardDTO.builder()
                .id(b.getId())
                .boardWriter(b.getBoardWriter())
                .boardPass(b.getBoardPass())
                .boardContents(b.getBoardContents())
                .boardTitle(b.getBoardTitle())
                .boardHits(b.getBoardHits())
                .createdAt(b.getCreatedAt())
                .build();
    }
}
