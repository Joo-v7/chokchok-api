package com.chokchok.chokchokapi.member.controller;

import com.chokchok.chokchokapi.common.aop.annotation.CheckRole;
import com.chokchok.chokchokapi.common.dto.ResponseDto;
import com.chokchok.chokchokapi.member.dto.request.MemberEmailUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberPasswordUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberRegisterRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberUsernameUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.response.MemberRegisterResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberStatusResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberUpdateResponseDto;
import com.chokchok.chokchokapi.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 회원에 관련된 ResponseController
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * POST 요청 - 회원가입
     * @param memberRegisterRequestDto
     * @return MemberRegisterResponseDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<MemberRegisterResponseDto> register(
            @Valid @RequestBody MemberRegisterRequestDto memberRegisterRequestDto
    ) {
        MemberRegisterResponseDto response = memberService.register(memberRegisterRequestDto);
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
     * @return MemberUpdateResponseDto
     */
    @CheckRole(hasAnyRole = {"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/username")
    public ResponseDto<MemberUpdateResponseDto> updateMemberUsername(
            @RequestHeader("X-MEMBER-ID") Long memberId,
            @Valid @RequestBody MemberUsernameUpdateRequestDto memberUsernameUpdateRequestDto
    ) {
        MemberUpdateResponseDto response = memberService.updateUsername(memberId, memberUsernameUpdateRequestDto);
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
     * @return MemberUpdateResponseDto
     */
    @CheckRole(hasAnyRole = {"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/email")
    public ResponseDto<MemberUpdateResponseDto> updateMemberEmail(
            @RequestHeader("X-MEMBER-ID") Long memberId,
            @Valid @RequestBody MemberEmailUpdateRequestDto memberEmailUpdateRequestDto
    ) {
        MemberUpdateResponseDto response = memberService.updateEmail(memberId, memberEmailUpdateRequestDto);
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
     * @return MemberUpdateResponseDto
     */
    @CheckRole(hasAnyRole = {"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/password")
    public ResponseDto<MemberUpdateResponseDto> updateMemberPassword(
            @RequestHeader("X-MEMBER-ID") Long memberId,
            @Valid @RequestBody MemberPasswordUpdateRequestDto memberPasswordUpdateRequestDto
    ) {
        MemberUpdateResponseDto response = memberService.updatePassword(memberId, memberPasswordUpdateRequestDto);
        return ResponseDto.<MemberUpdateResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * DELETE 요청 - 회원 탈퇴
     * @param memberId
     * @return MemberStatusResponseDto
     */
    @CheckRole(hasAnyRole = {"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<MemberStatusResponseDto> withdraw(@RequestHeader("X-MEMBER-ID") Long memberId) {
        MemberStatusResponseDto response = memberService.withdraw(memberId);
        return ResponseDto.<MemberStatusResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

}
