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
    @Column(length =300, nullable = true)
    public String originalFileName;
    @Column(length =500, nullable = true)
    public String storedFileName;
    @Column()
    public Long bardId;
}
