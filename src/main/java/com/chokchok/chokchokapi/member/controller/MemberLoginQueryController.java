package com.chokchok.chokchokapi.member.controller;

import com.chokchok.chokchokapi.common.dto.ResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberLoginInfoResponseDto;
import com.chokchok.chokchokapi.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 login 처리를 위한 RestController
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberLoginQueryController {

    private final MemberQueryService memberQueryService;

    /**
     * 회원이 로그인에 사용하는 id인 email로 회원의 정보와 권한을 조회 후 반환합니다.
     * @param email
     * @return MemberLoginResponseDto
     */
    @GetMapping("/login/{email}")
    public ResponseDto<MemberLoginInfoResponseDto> doLogin(@PathVariable String email) {
        MemberLoginInfoResponseDto memberLoginInfoResponseDto = memberQueryService.findMemberLoginInfoByEmail(email);
        return ResponseDto.<MemberLoginInfoResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(memberLoginInfoResponseDto)
                .build();
    }

}
