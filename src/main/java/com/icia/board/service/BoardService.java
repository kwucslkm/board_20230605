package com.icia.board.service;

import com.icia.board.dto.BoardDTO;
import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.BoardFileEntity;
import com.icia.board.repository.BoardFileReposity;
import com.icia.board.repository.BoardRepository;
import com.icia.board.util.UtilClass;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileReposity boardFileReposity;

    public Long save(BoardDTO boardDTO) throws IOException {
//        BoardEntity boardEntity = BoardEntity.saveToBoardEntity(boardDTO);
//        boardRepository.save(boardEntity);
        if (boardDTO.getBoardFile().get(0).isEmpty()) {
            //파일 없음
            BoardEntity boardEntity = BoardEntity.saveToBoardEntity(boardDTO);
            return boardRepository.save(boardEntity).getId();
        } else {
            //파일 있음
            //1. Board 테이블에 데이터를 먼저 저장
            BoardEntity boardEntity = BoardEntity.SaveToEntityWithFile(boardDTO);
            BoardEntity savedEntity = boardRepository.save(boardEntity);
            // 2. 파일이름 꺼내고, 저장용 이름 만들고 파일 로컬에 저장
            for(MultipartFile boardFile :boardDTO.getBoardFile()) {
                String originalFileName = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
                String savePath = "D:\\springboot_img\\" + storedFileName;
                boardFile.transferTo(new File(savePath));
                //3. BoardFileEntity로 변환 후 board_file_table에 저장
                // 자식 데이터를 저장할 때 반드시 부모의 id가 아닌 부모의 Entity 객체가 전달되어야 함.
                BoardFileEntity boardFileEntity =
                        BoardFileEntity.toSaveBoardFileEntity(savedEntity, originalFileName, storedFileName);
                boardFileReposity.save(boardFileEntity);
            }
            return savedEntity.getId();
        }
    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
//        for(BoardEntity b : boardEntities){
//            boardDTOList.add(BoardDTO.toDTO(b));
//        }
        boardEntities.forEach(boardEntity -> {
            boardDTOList.add(BoardDTO.toDTO(boardEntity));
        });
        return boardDTOList;
    }

    @Transactional
    public BoardDTO findById(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        return BoardDTO.toDTO(boardEntity);
//        return BoardDTO.toDTO(boardRepository.findById(id));
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
        System.out.println("id = " + id);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public void update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.saveToBoardEntityInId(boardDTO);
        boardRepository.save(boardEntity);
    }

    public Page<BoardDTO> paging(Pageable pageable, String type, String q) {
        int page = pageable.getPageNumber() - 1; // 시작페이지
        int pageLimit = 5; // 한 화면에 5글씩 보겠다.
        Page<BoardEntity> boardEntities = null;
        if(type.equals("title")){
            boardEntities = boardRepository.findByBoardTitleContaining(q, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));


        }else if(type.equals("writer")){
            boardEntities = boardRepository.findByBoardWriterContaining(q, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        }else {
            boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        }

        Page<BoardDTO> boardDTOS = boardEntities.map(boardEntity -> BoardDTO.builder()
                .id(boardEntity.getId())
                .boardTitle(boardEntity.getBoardTitle())
                .boardWriter(boardEntity.getBoardWriter())
                .createdAt(UtilClass.dateFormat(boardEntity.getCreatedAt()))
                .boardHits(boardEntity.getBoardHits())
                .build());
        return boardDTOS;
    }
}
