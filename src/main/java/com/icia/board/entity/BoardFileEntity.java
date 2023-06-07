package com.icia.board.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "board_file_table")
public class BoardFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(length =300, nullable = false)
    public String originalFileName;
    @Column(length =500, nullable = false)
    public String storedFileName;
    @ManyToOne(fetch = FetchType.LAZY) // 부모 조회 시, 필요하면 메소드 호출 해서 자식 데이타 함께 가져와, eager 일때는 무조건 다가져와
    @JoinColumn(name = "board_id")// 실제 DB에 생서되는 참조 컬럼의 이름
    private BoardEntity boardEntity; // 부모 엔티티 타입으로 정의

    public static BoardFileEntity toSaveBoardFileEntity(BoardEntity savedEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setBoardEntity(savedEntity);
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        return boardFileEntity;
    }
}
