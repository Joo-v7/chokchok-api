package com.chokchok.chokchokapi.member.service;

import com.chokchok.chokchokapi.common.exception.base.ConflictException;
import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.member.domain.Member;
import com.chokchok.chokchokapi.member.domain.MemberGrade;
import com.chokchok.chokchokapi.member.domain.MemberRole;
import com.chokchok.chokchokapi.member.dto.MemberRegisterRequestDto;
import com.chokchok.chokchokapi.member.dto.MemberResponseDto;
import com.chokchok.chokchokapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 조회/등록/수정/삭제
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleService memberRoleService;
    private final MemberGradeService memberGradeService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 조회 by id(P.K.)
     * @param id
     * @return MemberResponseDto
     */
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, "Member Not Found with: {}" + id));
        return MemberResponseDto.from(member);
    }

    /**
     * 회원 등록(회원 가입)
     * @param memberRegisterRequestDto
     * @return
     */
    @Transactional
    public MemberResponseDto register(MemberRegisterRequestDto memberRegisterRequestDto) {
        // username, email 중복 체크
        checkDuplication(memberRegisterRequestDto.username(), memberRegisterRequestDto.email());

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(memberRegisterRequestDto.password());

        // Default MemberRole, MemberGrade
        MemberRole memberRole = memberRoleService.getDefaultMemberRoleEntity();
        MemberGrade memberGrade = memberGradeService.getDefaultMemberGradeEntity();

        // Member Entity 생성
        Member member = Member.create(
                memberRole,
                memberGrade,
                memberRegisterRequestDto.email(),
                encodedPassword,
                memberRegisterRequestDto.username(),
                memberRegisterRequestDto.dateOfBirth(),
                memberRegisterRequestDto.gender()
        );

        try {
            Member savedMember = memberRepository.save(member);
            return MemberResponseDto.from(savedMember);
        } catch (DataIntegrityViolationException e) {
            log.error("회원 등록 중 데이터 무결성 위반 발생: {}", e.getMessage());
            throw new ConflictException(ErrorCode.MEMBER_ALREADY_EXISTS, "이미 존재하는 회원입니다.");
        } catch(Exception e) {
            log.error("회원가입 중 알 수 없는 오류 발생",e);
            throw new RuntimeException("회원가입 중 알 수 없는 오류가 발생했습니다.");
        }

    }

    /**
     * 회원가입 시 중복 사항을 체크하는 메소드
     * @param username 조회 대상
     * @param email 조회 대상
     */
    private void checkDuplication(String username, String email) {
        if (memberRepository.existsMemberByUsername(username)) {
            throw new ConflictException(ErrorCode.MEMBER_NAME_ALREADY_EXISTS, "이미 사용중인 사용자 이름입니다.");
        }

        if (memberRepository.existsMemberByEmail(email)) {
            throw new ConflictException(ErrorCode.MEMBER_EMAIL_ALREADY_EXISTS, "이미 사용중인 이메일입니다.");
        }
    }

    /**
     * username 중복 체크
     * @param username
     * @return boolean
     */
    public boolean existsByUsername(String username) {
        return memberRepository.existsMemberByUsername(username);
    }

    /**
     * email 중복 체크
     * @param email
     * @return boolean
     */
    public boolean existsByEmail(String email) {
        return memberRepository.existsMemberByEmail(email);
    }


}
