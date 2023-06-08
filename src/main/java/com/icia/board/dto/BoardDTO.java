package com.icia.board.dto;

import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.BoardFileEntity;
import com.icia.board.util.UtilClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardTitle;
    private String boardPass;
    private String boardContents;
    private int boardHits;
    private String  createdAt;
    private List<MultipartFile> boardFile;
    private int fileAttached;
    private List<String> storedFileName;
    private List<String> originalFileName;


    public static BoardDTO toDTO(BoardEntity b) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(b.getId());
        boardDTO.setBoardPass(b.getBoardPass());
        boardDTO.setBoardWriter(b.getBoardWriter());
        boardDTO.setBoardTitle(b.getBoardTitle());
        boardDTO.setBoardContents(b.getBoardContents());
        boardDTO.setBoardHits(b.getBoardHits());
        boardDTO.setCreatedAt(UtilClass.dateFormat(b.getCreatedAt()));

        // 파일 여부에 따른 코드 추가
        if (b.getFileAttached()==1){
            boardDTO.setFileAttached(1);
            //파일 이름을 담을 리스트 객체 선언
            List<String> originalFileNameList = new ArrayList<>();
            List<String > storedFileNameList = new ArrayList<>();
            for(BoardFileEntity boardFileEntity : b.getBoardFileEntityList()){
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);

        }else {
            boardDTO.setFileAttached(0);
        }
        return boardDTO;
//        return BoardDTO.builder()
//                .id(b.getId())
//                .boardWriter(b.getBoardWriter())
//                .boardPass(b.getBoardPass())
//                .boardContents(b.getBoardContents())
//                .boardTitle(b.getBoardTitle())
//                .boardHits(b.getBoardHits())
//                .createdAt(b.getCreatedAt())
//                .build();
    }
}
