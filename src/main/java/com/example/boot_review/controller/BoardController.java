package com.example.boot_review.controller;

import com.example.boot_review.dto.BoardDTO;
import com.example.boot_review.service.BoardService;
import com.example.boot_review.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/save-form") //글작성화면
    public String saveForm(){
        return "boardPages/save";
    }

    @GetMapping("/save") //글작성 및 파일
    public String save(@ModelAttribute BoardDTO boardDTO, Model model) throws IOException{
        boardService.save(boardDTO);
        return "redirect:/board";
    }
}
