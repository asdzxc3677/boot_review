package com.example.boot_review.service;

import com.example.boot_review.dto.MemberDTO;
import com.example.boot_review.entity.MemberEntity;
import com.example.boot_review.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Long save(MemberDTO memberDTO) throws IOException { //회원가입
        MultipartFile memberProFile = memberDTO.getMemberProFile();
        String memberProFileName = memberProFile.getOriginalFilename();
        memberProFileName = System.currentTimeMillis() + "_" + memberProFileName;
        String savePath = "D:\\springboot_img\\" + memberProFileName;
        if (!memberProFile.isEmpty()){
            memberProFile.transferTo(new File(savePath));
        }
        memberDTO.setMemberProFileName(memberProFileName);
        MemberEntity memberEntity = MemberEntity.entitySave(memberDTO);
        return memberRepository.save(memberEntity).getId();
    }

    public String idCheck(String id) { //아이디 중복체크
        Optional<MemberEntity> member = memberRepository.findByMemberEmail(id);
        if (member.isPresent()){
            return "no";
        }else {
            return "ok";
        }
    }


//    public MemberDTO login(MemberDTO memberDTO) { // 로그인 처리
//        MemberEntity member = loginCheck(memberDTO.getMemberEmail());
//        if (member.getMemberPassword().equals(memberDTO.getMemberPassword())){
//            return MemberDTO.saveDTO(member);
//        }else {
//            return null;
//        }
//    }
//
//    public MemberEntity loginCheck(String memberEmail) { // 로그인 처리
//        Optional<MemberEntity> member = memberRepository.findByMemberEmail(memberEmail);
//        if (member.isPresent()){
//            return member.get();
//        }else {
//            return null;
//        }
//    }

    public MemberDTO duplicateCheck(MemberDTO memberDTO) {  // 로그인폼에서 입력한 memberEmail, memberPassword가 들어있음
        Optional<MemberEntity> member = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        // 회원들 중에 입력한 이메일을 가진 회원이 있는지 findBy 한 것을 옵셔널(박스)에 담아옴
        if (member.isPresent()) {   // 박스안에 내용물이 있으면 (입력한 이메일을 가진 회원이 있을때)
            if (memberDTO.getMemberPassword().equals(member.get().getMemberPassword())) {
                // 내가 입력한 비밀번호랑, find 해서 찾아온 객체의 비밀번호와 같은지 비교함
                MemberDTO loginResult = MemberDTO.saveDTO(member.get());
                // Entity를 DTO 로 변환하고 그 값을 리턴
                return loginResult;
            } else {
                // 내가 입력한 비밀번호랑, find 해서 찾아온 객체의 비밀번호와 같지 않으면
                // null 리턴
                return null;
            }
        } else {
            // 회원들 중에 입력한 이메일을 가진 회원이 없으면
            // null 리턴
            return null;
        } // duplicateCheck = 중복확인
    }


    public List<MemberDTO> findAll(MemberDTO memberDTO) { //회원목록
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity: memberEntityList){
            memberDTOList.add(MemberDTO.saveDTO(memberEntity));
        }
        return memberDTOList;
    }

    public void delete(Long id) { //회원삭제
        memberRepository.deleteById(id);
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> om = memberRepository.findById(id);
        if (om.isPresent()){
            MemberEntity memberEntity = om.get();
            return MemberDTO.saveDTO(memberEntity);
        }else {
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.entityUpdate(memberDTO));
    }
}
