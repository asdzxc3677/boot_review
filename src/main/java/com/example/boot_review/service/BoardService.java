package com.example.boot_review.service;

import com.example.boot_review.common.PagingConst;
import com.example.boot_review.dto.BoardDTO;
import com.example.boot_review.entity.BoardEntity;
import com.example.boot_review.entity.MemberEntity;
import com.example.boot_review.repository.BoardRepository;
import com.example.boot_review.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Long save(BoardDTO boardDTO) throws IOException {
        MultipartFile boardFile = boardDTO.getBoardFile();
        String boardFileName = boardFile.getOriginalFilename();
        boardFileName = System.currentTimeMillis() + "_" + boardFileName;
        String savePath = "D:\\springboot_img\\" + boardFileName;
        if (!boardFile.isEmpty()) {
            boardFile.transferTo(new File(savePath));
        }
        boardDTO.setBoardFileName(boardFileName);

        Optional<MemberEntity> optionalMemberEntity =
                memberRepository.findByMemberEmail(boardDTO.getBoardWriter());
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            Long saveId = boardRepository.save(BoardEntity.entitySave(boardDTO, memberEntity)).getId();
            return saveId;
        } else {
            return null;
        }
    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toFind(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional
    public BoardDTO findById(Long id) {
        boardRepository.boardHits(id);
        Optional<BoardEntity> ob = boardRepository.findById(id);
        if (ob.isPresent()) {
            BoardEntity boardEntity = ob.get();
            return BoardDTO.toFind(boardEntity);
        } else {
            return null;
        }
    }


    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber(); // 요청 페이지값 가져옴.
        // 요청한 페이지가 1이면 페이지값을 0으로 하고 1이 아니면 요청 페이지에서 1을 뺀다.
//        page = page - 1;
        // 삼항연산자
        page = (page == 1)? 0: (page-1);
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        // Page<BoardEntity> => Page<BoardDTO>
        Page<BoardDTO> boardList = boardEntities.map(
                // BoardEntity 객체 -> BoardDTO 객체 변환
                // board: BoardEntity 객체
                // new BoardDTO() 생성자
                board -> new BoardDTO(board.getId(),
                        board.getBoardTitle(),
                        board.getBoardWriter(),
                        board.getBoardContents(),
                        board.getBoardHits(),
                        board.getCreatedTime()
                ));
        return boardList;
    }

    public void update(BoardDTO boardDTO) {
        boardRepository.save(BoardEntity.entityFind(boardDTO));
    }
}
