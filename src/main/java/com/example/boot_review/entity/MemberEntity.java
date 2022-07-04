package com.example.boot_review.entity;

import com.example.boot_review.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "member_table1")
public class MemberEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_Email", length = 20, nullable = false)
    private String memberEmail;

    @Column(name = "member_Password", length = 20, nullable = false)
    private String memberPassword;

    @Column(name = "member_Name", length = 20)
    private String memberName;

    @Column(name = "member_Mail", length = 30)
    private String memberMail;

    @Column(name = "member_Mobile", length = 20, nullable = false)
    private String memberMobile;

    @Column(name = "member_Pro_File_Name", length = 50)
    private String memberProFileName;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.PERSIST,
            orphanRemoval = false, fetch = FetchType.LAZY)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.PERSIST,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        boardEntityList.forEach(board -> board.setMemberEntity(null));
        commentEntityList.forEach(comment -> comment.setMemberEntity(null));
    }

    public static MemberEntity entitySave(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMail(memberDTO.getMemberMail());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setMemberProFileName(memberDTO.getMemberProFileName());
        return memberEntity;
    }
    public static MemberEntity entityUpdate(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMail(memberDTO.getMemberMail());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setMemberProFileName(memberDTO.getMemberProFileName());
        return memberEntity;
    }
}