package com.chokchok.chokchokapi.member.service;

import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.member.domain.Member;
import com.chokchok.chokchokapi.member.dto.response.MemberLoginInfoResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberResponseDto;
import com.chokchok.chokchokapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 조회를 담당하는 서비스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QueryMemberService {

    private final MemberRepository memberRepository;

    /**
     * 이메일로 로그인에 필요한 정보 조회
     * @param email
     * @return MemberLoginResponseDto
     */
    @Transactional(readOnly = true)
    public MemberLoginInfoResponseDto findMemberLoginInfoByEmail(String email) {
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, "Member Not Found with email: " + email));
        return MemberLoginInfoResponseDto.from(member);
    }

    /**
     * 회원 조회 by id(P.K.)
     * @param memberId
     * @return MemberResponseDto
     */
    @Transactional(readOnly = true)
    public MemberResponseDto findMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, "Member Not Found with id: " + memberId));
        return MemberResponseDto.from(member);
    }

    /**
     * username 중복 체크
     * @param username
     * @return boolean
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return memberRepository.existsMemberByUsername(username);
    }

    /**
     * email 중복 체크
     * @param email
     * @return boolean
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return memberRepository.existsMemberByEmail(email);
    }

}
