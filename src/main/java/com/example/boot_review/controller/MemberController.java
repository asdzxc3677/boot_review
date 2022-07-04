package com.example.boot_review.controller;

import com.example.boot_review.dto.MemberDTO;
import com.example.boot_review.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("save-form") //회원가입 화면
    public String saveForm() {
        return "memberPages/save";
    }

    @PostMapping("/save") //회원가입 처리
    public String save(@ModelAttribute MemberDTO memberDTO) throws IOException {
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return "memberPages/login";
    }

    @PostMapping("/idCheck") // 아이디 중복체크
    public @ResponseBody String idCheck(@RequestParam("id") String id) {
        String result = memberService.idCheck(id);
        return result;
    }


    @GetMapping("/login-form") //로그인화면
    public String loginForm() {
        return "memberPages/login";
    }

    @PostMapping("/login") // 로그인처리
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginDTO = memberService.duplicateCheck(memberDTO);
        System.out.println("loginDTO = " + loginDTO);
        if (loginDTO != null) {
            session.setAttribute("loginId", loginDTO.getId());
            session.setAttribute("loginEmail", loginDTO.getMemberEmail());
            session.setAttribute("password", loginDTO.getMemberPassword());
            return "redirect:/board";
        } else {
            return "memberPages/login";
        }
    }

    @GetMapping("/logout") //로그아웃
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/findAll") //회원목록
    public String findAll(@ModelAttribute MemberDTO memberDTO, Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll(memberDTO);
        model.addAttribute("member", memberDTOList);
        return "memberPages/list";
    }

    @GetMapping("/admin") // 관리자페이지
    public String admin() {
        return "redirect:/member/findAll";
    }

    @GetMapping("/delete/{id}") // 회원삭제
    public String delete(@PathVariable("id") Long id, HttpSession session) {
        memberService.delete(id);
        if (session.getAttribute("loginEmail") != "admin") { //관리자가 아닐때에 회원탈퇴를 누루면 세션값 삭제
            session.invalidate();
        }
        return "index";
    }

    @GetMapping("/myPage/{id}") //나의페이지 띄우기
    public String myPage(@PathVariable("id") Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/myPage";
    }

    @GetMapping("/update/{id}") //수정화면 띄우기
    public String updateForm(@PathVariable("id") Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/update";
    }

    @PostMapping("/update") //수정처리
    public String update(@ModelAttribute MemberDTO memberDTO, Model model){
        memberService.update(memberDTO);
        model.addAttribute("member",memberService.findById(memberDTO.getId()));
        return "redirect:/member/myPage/" + memberDTO.getId();
}

}
