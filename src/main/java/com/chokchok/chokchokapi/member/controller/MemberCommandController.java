package com.chokchok.chokchokapi.member.controller;

import com.chokchok.chokchokapi.common.aop.annotation.CheckRole;
import com.chokchok.chokchokapi.common.dto.ResponseDto;
import com.chokchok.chokchokapi.member.dto.request.*;
import com.chokchok.chokchokapi.member.dto.response.MemberRegisterResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberStatusResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberUpdateResponseDto;
import com.chokchok.chokchokapi.member.service.MemberCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 등록/수정/삭제를 위한 RestController
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberCommandController {

    private final MemberCommandService memberCommandService;

    private static final String X_MEMBER_ID = "X-MEMBER-ID";

    /**
     * POST 요청 - 회원가입
     * @param memberRegisterRequestDto
     * @return ResponseDto<MemberRegisterResponseDto>
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<MemberRegisterResponseDto> register(
            @Valid @RequestBody MemberRegisterRequestDto memberRegisterRequestDto
    ) {
        MemberRegisterResponseDto response = memberCommandService.register(memberRegisterRequestDto);
        return ResponseDto.<MemberRegisterResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    /**
     * PUT 요청 - 회원 username
     * @param memberId
     * @param memberUsernameUpdateRequestDto
     * @return ResponseDto<MemberUpdateResponseDto>
     */
    @CheckRole(hasAnyRole = {"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/username")
    public ResponseDto<MemberUpdateResponseDto> updateMemberUsername(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @Valid @RequestBody MemberUsernameUpdateRequestDto memberUsernameUpdateRequestDto
    ) {
        MemberUpdateResponseDto response = memberCommandService.updateUsername(memberId, memberUsernameUpdateRequestDto);
        return ResponseDto.<MemberUpdateResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * PUT 요청 - 회원 email
     * @param memberId
     * @param memberEmailUpdateRequestDto
     * @return ResponseDto<MemberUpdateResponseDto>
     */
    @CheckRole(hasAnyRole = {"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/email")
    public ResponseDto<MemberUpdateResponseDto> updateMemberEmail(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @Valid @RequestBody MemberEmailUpdateRequestDto memberEmailUpdateRequestDto
    ) {
        MemberUpdateResponseDto response = memberCommandService.updateEmail(memberId, memberEmailUpdateRequestDto);
        return ResponseDto.<MemberUpdateResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * PUT 요청 - 회원 password
     * @param memberId
     * @param memberPasswordUpdateRequestDto
     * @return ResponseDto<MemberUpdateResponseDto>
     */
    @CheckRole(hasAnyRole = {"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/password")
    public ResponseDto<MemberUpdateResponseDto> updateMemberPassword(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @Valid @RequestBody MemberPasswordUpdateRequestDto memberPasswordUpdateRequestDto
    ) {
        MemberUpdateResponseDto response = memberCommandService.updatePassword(memberId, memberPasswordUpdateRequestDto);
        return ResponseDto.<MemberUpdateResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * DELETE 요청 - 회원탈퇴
     * @param memberId
     * @return ResponseDto<MemberStatusResponseDto>
     */
    @CheckRole(hasAnyRole = {"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<MemberStatusResponseDto> withdraw(@RequestHeader(X_MEMBER_ID) Long memberId) {
        MemberStatusResponseDto response = memberCommandService.withdraw(memberId);
        return ResponseDto.<MemberStatusResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

}
