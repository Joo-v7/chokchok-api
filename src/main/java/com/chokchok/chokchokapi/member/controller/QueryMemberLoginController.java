package com.chokchok.chokchokapi.member.controller;

import com.chokchok.chokchokapi.common.dto.ResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberLoginResponseDto;
import com.chokchok.chokchokapi.member.service.QueryMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 login 처리를 위해 Auth 서버와 통신하는 API 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class QueryMemberLoginController {

    private final QueryMemberService queryMemberService;

    /**
     * 회원이 로그인에 사용하는 email 로 회원의 정보와 권한을 조회
     * @param email
     * @return MemberLoginResponseDto
     */
    @GetMapping("/login/{email}")
    public ResponseDto<MemberLoginResponseDto> doLogin(@PathVariable String email) {
        MemberLoginResponseDto memberLoginResponseDto = queryMemberService.findMemberLoginInfoByEmail(email);
        return ResponseDto.<MemberLoginResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(memberLoginResponseDto)
                .build();
    }

}
