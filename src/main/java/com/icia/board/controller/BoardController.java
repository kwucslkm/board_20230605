package com.icia.board.controller;

import com.icia.board.dto.BoardDTO;
import com.icia.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/save")
    public String saveForm(){
        return "boardPages/boardSave";
    }
    @PostMapping("/save")
    public ResponseEntity save(@RequestBody BoardDTO boardDTO){
        boardService.save(boardDTO);
        return new ResponseEntity(boardDTO,HttpStatus.OK);
    }
    @GetMapping("/")
    public String findAll(Model model){
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("list",boardDTOList);
        return "boardPages/boardList";
    }
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model){
        boardService.updateHits(id);
        BoardDTO boardDTO = null;
        try {
            boardDTO = boardService.findById(id);

        }catch (NoSuchElementException e){
            return "boardPages/boardNotFound";
        }
        model.addAttribute("board",boardDTO);
        return "boardPages/boardDetail";
    }
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){

        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board",boardDTO);
        return "boardPages/boardUpdate";
    }
    @PostMapping("/update/{id}")
    public String update(@ModelAttribute BoardDTO boardDTO){
        System.out.println("수정에서boardDTO = " + boardDTO);
        boardService.update(boardDTO);
        return "redirect:/";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
         boardService.delete(id);
        return "redirect:/";
    }




}
