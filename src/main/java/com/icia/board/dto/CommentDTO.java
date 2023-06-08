package com.icia.board.dto;

import com.icia.board.entity.CommentEntity;
import com.icia.board.util.UtilClass;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private String  createdAt;
    private String  updatedAt;

    public static CommentDTO toDTO(CommentEntity comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentDTO.getId());
        commentDTO.setCommentWriter(comment.getCommentWriter());
        commentDTO.setCommentContents(comment.getCommentContents());
        commentDTO.setBoardId(comment.getBoardEntity().getId());
        commentDTO.setCreatedAt(UtilClass.dateFormat(comment.getCreatedAt()));
        commentDTO.setUpdatedAt(UtilClass.dateFormat(comment.getUpdatedAt()));
        return commentDTO;


    }
}
