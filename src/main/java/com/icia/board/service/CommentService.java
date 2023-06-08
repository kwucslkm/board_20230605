package com.icia.board.service;

import com.icia.board.dto.CommentDTO;
import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.CommentEntity;
import com.icia.board.repository.BoardRepository;
import com.icia.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardService boardService;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    public Long save(CommentDTO commentDTO) {
        BoardEntity boardEntity = boardRepository.findById(commentDTO.getBoardId()).orElseThrow(() -> new NoSuchElementException());
        CommentEntity commentEntity = CommentEntity.toSaveEntity(boardEntity, commentDTO);
        return commentRepository.save(commentEntity).getId();
    }

@Transactional
    public List<CommentDTO> findAll(Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(()-> new NoSuchElementException());
        List<CommentEntity> commentEntityList = commentRepository.findByBoardEntity(boardEntity);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentEntityList.forEach(comment->{
            commentDTOList.add(CommentDTO.toDTO(comment));
        });
        return commentDTOList;
    }
}
