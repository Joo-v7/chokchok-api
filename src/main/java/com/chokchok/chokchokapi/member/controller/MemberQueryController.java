package com.chokchok.chokchokapi.member.controller;

import com.chokchok.chokchokapi.common.aop.annotation.CheckRole;
import com.chokchok.chokchokapi.common.dto.ResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberResponseDto;
import com.chokchok.chokchokapi.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 조회를 위한 RestController
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberQueryController {

    private final MemberQueryService memberQueryService;

    private static final String X_MEMBER_ID = "X-MEMBER-ID";

    /**
     * 회원의 정보를 조회
     * @param memberId
     * @return MemberResponseDto
     */
    @GetMapping("/info")
    @CheckRole(hasAnyRole = {"ROLE_USER", "ROLE_ADMIN"})
    public ResponseDto<MemberResponseDto> getMemberInfo(@RequestHeader(X_MEMBER_ID) Long memberId) {
        MemberResponseDto response = memberQueryService.findMemberById(memberId);

        return ResponseDto.<MemberResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 회원가입 시 username의 중복 여부를 판별 하기 위한 기능
     * @param username
     * @return boolean - username의 중복 여부
     */
    @GetMapping("/checkUsername/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Boolean> checkUsername(@PathVariable String username) {
        boolean response = memberQueryService.existsByUsername(username);
        return ResponseDto.<Boolean>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 회원가입 시 email의 중복 여부를 판별하기 위한 기능
     * @param email
     * @return boolean - email의 중복 여부
     */
    @GetMapping("/checkEmail/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Boolean> checkEmail(@PathVariable String email) {
        boolean response = memberQueryService.existsByEmail(email);
        return ResponseDto.<Boolean>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }


}
