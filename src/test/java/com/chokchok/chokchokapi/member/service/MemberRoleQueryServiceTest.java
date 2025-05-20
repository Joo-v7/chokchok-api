package com.chokchok.chokchokapi.member.service;

import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.member.repository.MemberRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MemberRoleQueryServiceTest {

    @InjectMocks
    private MemberRoleQueryService memberRoleQueryService;

    @Mock
    private MemberRoleRepository memberRoleRepository;

    @Test
    @DisplayName("Default MemberRole을 가져오지 못하면 예외가 발생한다.")
    void fail_getDefaultMemberRoleEntity() {
        // when
        Mockito.when(memberRoleRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(NotFoundException.class,
                () -> memberRoleQueryService.getDefaultMemberRoleEntity());

    }

}