package com.example.boot_review.service;

import com.example.boot_review.dto.CommentDTO;
import com.example.boot_review.entity.CommentEntity;
import com.example.boot_review.repository.BoardRepository;
import com.example.boot_review.repository.CommentRepository;
import com.example.boot_review.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;


    public List<CommentDTO> findAll(Long id) {
        List<CommentEntity> commentEntityList = commentRepository.findByBoardNumber(id);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(CommentEntity commentEntity: commentEntityList) {
            commentDTOList.add(CommentDTO.findDTO(commentEntity));
        } return commentDTOList;
    }
}
