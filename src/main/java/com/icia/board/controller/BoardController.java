package com.icia.board.controller;

import com.icia.board.dto.BoardDTO;
import com.icia.board.dto.CommentDTO;
import com.icia.board.service.BoardService;
import com.icia.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/save")
    public String saveForm(){
        return "boardPages/boardSave";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "redirect:/board/";
    }
    @GetMapping("/")
    public String findAll(Model model){
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("list",boardDTOList);
        return "boardPages/boardList";
    }
    // /board?page=1
    @GetMapping
    public String paging(@PageableDefault(page = 1) Pageable pageable,
                         @RequestParam(value = "type", required = false, defaultValue = "")  String type,
                         @RequestParam(value = "q", required = false, defaultValue = "") String  q,
                         Model model) {
        System.out.println("page = " + pageable.getPageNumber());
        Page<BoardDTO> boardDTOS = boardService.paging(pageable, type, q);
        model.addAttribute("boardList", boardDTOS);
        // 시작페이지(startPage), 마지막페이지(endPage)값 계산
        // 하단에 보여줄 페이지 갯수 3개
        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardDTOS.getTotalPages()) ? startPage + blockLimit - 1 : boardDTOS.getTotalPages();
//        if ((startPage + blockLimit - 1) < boardDTOS.getTotalPages()) {
//            endPage = startPage + blockLimit - 1;
//        } else {
//            endPage = boardDTOS.getTotalPages();
//        }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("type", type);
        model.addAttribute("q", q);

        return "boardPages/boardPaging";

    }
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id,
                           @RequestParam("type") String type,
                           @RequestParam("q") String q,
                           @RequestParam("page") int page,
                           Model model){
        boardService.updateHits(id);
        model.addAttribute("type",type);
        model.addAttribute("page",page);
        model.addAttribute("q",q);
        BoardDTO boardDTO = null;
        try {
            boardDTO = boardService.findById(id);
            List<CommentDTO> commenttDTOList = commentService.findAll(id);
            if (commenttDTOList.size()>0){
            model.addAttribute("commentList",commenttDTOList);
                }else{
                model.addAttribute("commentList",null);
            }

        }catch (NoSuchElementException e){
            return "boardPages/boardNotFound";
        }
        model.addAttribute("board",boardDTO);
        return "boardPages/boardDetail";
    }
    @GetMapping("/update")
    public String updateForm(@RequestParam Long id,
                             @RequestParam("type") String type,
                             @RequestParam("q") String q,
                             @RequestParam("page") int page,
                             Model model){

        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board",boardDTO);
        model.addAttribute("q",q);
        model.addAttribute("type",type);
        model.addAttribute("page",page);
        return "boardPages/boardUpdate";
    }
    @PostMapping("/update/{id}")
    public ResponseEntity update(@ModelAttribute BoardDTO boardDTO,
                                 @RequestParam("type") String type,
                                 @RequestParam("q") String q,
                                 @RequestParam("page") int page,
                                 Model model){
        System.out.println("수정할getId() = " + boardDTO.getId());
        System.out.println("수정할boardDTO = " + boardDTO);
        System.out.println("type = " + type);
        System.out.println("q = " + q);
        System.out.println("page = " + page);
        boardService.update(boardDTO);
        model.addAttribute("q",q);
        model.addAttribute("type",type);
        model.addAttribute("page",page);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
         boardService.delete(id);
        return "redirect:/board/";

    }




}
