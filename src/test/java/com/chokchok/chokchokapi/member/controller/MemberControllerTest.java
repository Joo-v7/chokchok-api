package com.chokchok.chokchokapi.member.controller;

import com.chokchok.chokchokapi.common.aop.aspect.CheckRoleAspect;
import com.chokchok.chokchokapi.member.domain.Gender;
import com.chokchok.chokchokapi.member.domain.Member;
import com.chokchok.chokchokapi.member.domain.MemberGrade;
import com.chokchok.chokchokapi.member.domain.MemberRole;
import com.chokchok.chokchokapi.member.dto.request.MemberEmailUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberPasswordUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberRegisterRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberUsernameUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.response.MemberRegisterResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberStatusResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberUpdateResponseDto;
import com.chokchok.chokchokapi.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(CheckRoleAspect.class)
@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberService memberService;
    private Member member;
    private MemberRegisterResponseDto registerResponseDto;
    private MemberUpdateResponseDto updateResponseDto;
    private MemberStatusResponseDto statusResponseDto;

    private final String USERNAME = "촉촉";
    private final String EMAIL = "chokchok@chokchok.com";
    private final String PASSWORD = "chokchok";
    private final LocalDate BIRTH_DATE = LocalDate.of(2025, 4, 18);
    private final Gender GENDER = Gender.MALE;


    @BeforeEach
    void setUp() {
        MemberGrade memberGrade = MemberGrade.create("BASIC", 10, "일반 등급");
        MemberRole memberRole = MemberRole.create("user", "ROLE_USER");
        member = Member.create(memberRole, memberGrade, USERNAME, EMAIL, PASSWORD, BIRTH_DATE, GENDER);

    }

    @Test
    @DisplayName("회원가입 성공")
    void register_success() throws Exception {
        // given
        MemberRegisterRequestDto request = new MemberRegisterRequestDto(
                USERNAME,
                EMAIL,
                PASSWORD,
                BIRTH_DATE,
                GENDER
        );
        registerResponseDto = MemberRegisterResponseDto.from(member);
        Mockito.when(memberService.register(Mockito.any(MemberRegisterRequestDto.class))).thenReturn(registerResponseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.username").value(USERNAME))
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.dateOfBirth").value(BIRTH_DATE.toString()))
                .andExpect(jsonPath("$.data.gender").value(GENDER.getDisplayName()));

        Mockito.verify(memberService).register(Mockito.any(MemberRegisterRequestDto.class));
    }

    @Test
    @DisplayName("회원가입 시, 유효성 검증(@Valid)에 실패 - username cannot be null")
    void register_withInvalidUsername1() throws Exception {
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
                null,
                EMAIL,
                PASSWORD,
                BIRTH_DATE,
                GENDER
        );

        // given
        Mockito.when(memberService.register(Mockito.any(MemberRegisterRequestDto.class))).thenReturn(registerResponseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());
        Mockito.verify(memberService, Mockito.never()).register(Mockito.any());
    }

    @Test
    @DisplayName("회원가입 시, 유효성 검증(@Valid)에 실패 - username has an invalid length")
    void register_withInvalidUsername2() throws Exception {
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
                "a",
                EMAIL,
                PASSWORD,
                BIRTH_DATE,
                GENDER
        );

        // given
        Mockito.when(memberService.register(Mockito.any(MemberRegisterRequestDto.class))).thenReturn(registerResponseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());
        Mockito.verify(memberService, Mockito.never()).register(Mockito.any());
    }

    @Test
    @DisplayName("회원가입 시, 유효성 검증(@Valid)에 실패 - username has an invalid pattern")
    void register_withInvalidUsername3() throws Exception {
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
                "***",
                EMAIL,
                PASSWORD,
                BIRTH_DATE,
                GENDER
        );

        // given
        Mockito.when(memberService.register(Mockito.any(MemberRegisterRequestDto.class))).thenReturn(registerResponseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());
        Mockito.verify(memberService, Mockito.never()).register(Mockito.any());
    }

    @Test
    @DisplayName("회원가입 시, 유효성 검증(@Valid)에 실패 - email cannot be null")
    void register_withInvalidEmail1() throws Exception {
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
                USERNAME,
                null,
                PASSWORD,
                BIRTH_DATE,
                GENDER
        );

        // given
        Mockito.when(memberService.register(Mockito.any(MemberRegisterRequestDto.class))).thenReturn(registerResponseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());
        Mockito.verify(memberService, Mockito.never()).register(Mockito.any());
    }

    @Test
    @DisplayName("회원가입 시, 유효성 검증(@Valid)에 실패 - invalid email format or length")
    void register_withInvalidEmail2() throws Exception {
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
                USERNAME,
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@chokchok.com",
                PASSWORD,
                BIRTH_DATE,
                GENDER
        );

        // given
        Mockito.when(memberService.register(Mockito.any(MemberRegisterRequestDto.class))).thenReturn(registerResponseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());
        Mockito.verify(memberService, Mockito.never()).register(Mockito.any());
    }

    @Test
    @DisplayName("회원가입 시, 유효성 검증(@Valid)에 실패 - password cannot be null")
    void register_withInvalidPassword() throws Exception {
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
                USERNAME,
                EMAIL,
                null,
                BIRTH_DATE,
                GENDER
        );

        // given
        Mockito.when(memberService.register(Mockito.any(MemberRegisterRequestDto.class))).thenReturn(registerResponseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());
        Mockito.verify(memberService, Mockito.never()).register(Mockito.any());
    }

    @Test
    @DisplayName("회원가입 시, 유효성 검증(@Valid)에 실패 - dateOfBirth cannot be null")
    void register_withInvalidDateOfBirth() throws Exception {
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
                USERNAME,
                EMAIL,
                PASSWORD,
                null,
                GENDER
        );

        // given
        Mockito.when(memberService.register(Mockito.any(MemberRegisterRequestDto.class))).thenReturn(registerResponseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());
        Mockito.verify(memberService, Mockito.never()).register(Mockito.any());
    }

    @Test
    @DisplayName("회원가입 시, 유효성 검증(@Valid)에 실패 - gender cannot be null")
    void register_withInvalidGender() throws Exception {
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
                USERNAME,
                EMAIL,
                PASSWORD,
                BIRTH_DATE,
                null
        );

        // given
        Mockito.when(memberService.register(Mockito.any(MemberRegisterRequestDto.class))).thenReturn(registerResponseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        perform.andDo(print()).andExpect(status().isBadRequest());
        Mockito.verify(memberService, Mockito.never()).register(Mockito.any());
    }

    @Test
    @DisplayName("사용자 이름 수정 성공")
    void updateMemberUsername_success() throws Exception {
        MemberUsernameUpdateRequestDto requestDto = new MemberUsernameUpdateRequestDto("chokchok");

        // given
        Mockito.when(memberService.updateUsername(Mockito.anyLong() ,Mockito.any(MemberUsernameUpdateRequestDto.class))).thenReturn(updateResponseDto);

        // when
        ResultActions perform = mockMvc.perform(put("/api/members/username")
                .header("X-MEMBER-ID", 1L)
                .header("X-MEMBER-ROLE", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(memberService).updateUsername(Mockito.anyLong() ,Mockito.any(MemberUsernameUpdateRequestDto.class));

    }

    @Test
    @DisplayName("사용자 이름 수정 실패 1 - null")
    void updateMemberUsername_withNull() throws Exception {
        MemberUsernameUpdateRequestDto requestDto = new MemberUsernameUpdateRequestDto(null);

        // given
        Mockito.when(memberService.updateUsername(Mockito.anyLong() ,Mockito.any(MemberUsernameUpdateRequestDto.class))).thenReturn(updateResponseDto);

        // when
        ResultActions perform = mockMvc.perform(put("/api/members/username")
                .header("X-MEMBER-ID", 1L)
                .header("X-MEMBER-ROLE", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorCode").value(4002))
                .andExpect(jsonPath("$.message").value("[사용자 이름은 필수 입력 사항입니다.]"));
    }

    @Test
    @DisplayName("사용자 이름 수정 실패 2 - @Valid 검증 조건에 맞지 않은 경우")
    void updateMemberUsername_withInvalidInputData() throws Exception {
        MemberUsernameUpdateRequestDto requestDto = new MemberUsernameUpdateRequestDto(null);

        // given
        Mockito.when(memberService.updateUsername(Mockito.anyLong() ,Mockito.any(MemberUsernameUpdateRequestDto.class))).thenReturn(updateResponseDto);

        // when
        ResultActions perform = mockMvc.perform(put("/api/members/username")
                .header("X-MEMBER-ID", 1L)
                .header("X-MEMBER-ROLE", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorCode").value(4002))
                .andExpect(jsonPath("$.message").value("[사용자 이름은 필수 입력 사항입니다.]"));

    }


    @Test
    @DisplayName("이메일 수정 성공")
    void updateMemberEmail_success() throws Exception {
        MemberEmailUpdateRequestDto requestDto = new MemberEmailUpdateRequestDto("chokchok@chokchok.site");

        // given
        Mockito.when(memberService.updateEmail(Mockito.anyLong(), Mockito.any(MemberEmailUpdateRequestDto.class))).thenReturn(updateResponseDto);

        // when
        ResultActions perform = mockMvc.perform(put("/api/members/email")
                .header("X-MEMBER-ID", 1L)
                .header("X-MEMBER-ROLE", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(memberService).updateEmail(Mockito.anyLong(), Mockito.any(MemberEmailUpdateRequestDto.class));
    }

    @Test
    @DisplayName("이메일 수정 실패 1 - null")
    void updateMemberEmail_withNull() throws Exception {
        MemberEmailUpdateRequestDto requestDto = new MemberEmailUpdateRequestDto(null);

        // given
        Mockito.when(memberService.updateEmail(Mockito.anyLong(), Mockito.any(MemberEmailUpdateRequestDto.class))).thenReturn(updateResponseDto);

        // when
        ResultActions perform = mockMvc.perform(put("/api/members/email")
                .header("X-MEMBER-ID", 1L)
                .header("X-MEMBER-ROLE", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorCode").value(4002))
                .andExpect(jsonPath("$.message").value("[이메일은 필수 입력 사항입니다.]"));

    }

    @Test
    @DisplayName("이메일 수정 실패 2 - @Valid 검증 조건에 맞지 않은 경우")
    void updateMemberEmail_withInvalidInputData() throws Exception {
        MemberEmailUpdateRequestDto requestDto = new MemberEmailUpdateRequestDto("chokchok");

        // given
        Mockito.when(memberService.updateEmail(Mockito.anyLong(), Mockito.any(MemberEmailUpdateRequestDto.class))).thenReturn(updateResponseDto);

        // when
        ResultActions perform = mockMvc.perform(put("/api/members/email")
                .header("X-MEMBER-ID", 1L)
                .header("X-MEMBER-ROLE", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorCode").value(4002))
                .andExpect(jsonPath("$.message").value("[올바른 이메일 양식이 아닙니다.]"));
    }

    @Test
    @DisplayName("비밀번호 수정 성공")
    void updateMemberPassword_success() throws Exception {
        MemberPasswordUpdateRequestDto requestDto = new MemberPasswordUpdateRequestDto("chokchok");

        // given
        Mockito.when(memberService.updatePassword(Mockito.anyLong(), Mockito.any(MemberPasswordUpdateRequestDto.class))).thenReturn(updateResponseDto);

        // when
        ResultActions perform = mockMvc.perform(put("/api/members/password")
                .header("X-MEMBER-ID", 1L)
                .header("X-MEMBER-ROLE", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(memberService).updatePassword(Mockito.anyLong(), Mockito.any(MemberPasswordUpdateRequestDto.class));
    }

    @Test
    @DisplayName("비밀번호 수정 실패 - null")
    void updateMemberPassword_withNull() throws Exception {
        MemberPasswordUpdateRequestDto requestDto = new MemberPasswordUpdateRequestDto(null);

        // given
        Mockito.when(memberService.updatePassword(Mockito.anyLong(), Mockito.any(MemberPasswordUpdateRequestDto.class))).thenReturn(updateResponseDto);

        // when
        ResultActions perform = mockMvc.perform(put("/api/members/password")
                .header("X-MEMBER-ID", 1L)
                .header("X-MEMBER-ROLE", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errorCode").value(4002))
                .andExpect(jsonPath("$.message").value("[비밀번호는 필수 입력 사항입니다.]"));
    }

    @Test
    @DisplayName("회원탈퇴 성공")
    void withdraw_success() throws Exception {
        // given
        Mockito.when(memberService.withdraw(Mockito.anyLong())).thenReturn(statusResponseDto);

        // when
        ResultActions perform = mockMvc.perform(delete("/api/members/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-MEMBER-ID", 1L));

        // then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(memberService).withdraw(Mockito.anyLong());
    }

    @Test
    @DisplayName("회원탈퇴 실패")
    void withdraw_InvalidMemberID() throws Exception {
        // given
        Mockito.when(memberService.withdraw(Mockito.anyLong())).thenReturn(statusResponseDto);

        // when
        ResultActions perform = mockMvc.perform(delete("/api/members/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-MEMBER-ID", 1L));

        // then
        perform.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        Mockito.verify(memberService).withdraw(Mockito.anyLong());
    }
}