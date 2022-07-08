package com.example.boot_review.controller;

import com.example.boot_review.common.PagingConst;
import com.example.boot_review.dto.BoardDTO;
import com.example.boot_review.dto.CommentDTO;
import com.example.boot_review.service.BoardService;
import com.example.boot_review.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;

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

    @GetMapping("/findAll") //글목록
    public String findAll(@ModelAttribute BoardDTO boardDTO, Model model){
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList",boardDTOList);
        return "boardPages/detail";
    }

    @GetMapping("/{id}") // 글 상세조회
    public String findById(@PathVariable("id") Long id,Model model){
        BoardDTO boardDTO = boardService.findById(id);
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("comment", commentDTOList);
        return "boardPages/detail";
    }
    @GetMapping
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<BoardDTO> boardList = boardService.paging(pageable);
        model.addAttribute("boardList", boardList);
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < boardList.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : boardList.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardPages/list";
    }
}
