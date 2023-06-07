package com.icia.board.repository;

import com.icia.board.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileReposity extends JpaRepository<BoardFileEntity,Long> {

}
