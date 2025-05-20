package com.chokchok.chokchokapi.member.service;

import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.member.domain.MemberRole;
import com.chokchok.chokchokapi.member.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원권한 관련 서비스를 담당하는 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberRoleQueryService {

    private final MemberRoleRepository memberRoleRepository;

    private final static String DEFAULT_MEMBER_ROLE_NAME = "user";

    /**
     * Default Role을 가져오는 메소드
     * @return MemberRole
     */
    @Transactional(readOnly = true)
    public MemberRole getDefaultMemberRoleEntity() {
        return memberRoleRepository.findByName(DEFAULT_MEMBER_ROLE_NAME)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_ROLE_NOT_FOUND, "Default role not found"));
    }

}
