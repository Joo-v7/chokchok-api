package com.chokchok.chokchokapi.member.service;

import com.chokchok.chokchokapi.common.exception.base.ConflictException;
import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.member.domain.Gender;
import com.chokchok.chokchokapi.member.domain.Member;
import com.chokchok.chokchokapi.member.domain.MemberGrade;
import com.chokchok.chokchokapi.member.domain.MemberRole;
import com.chokchok.chokchokapi.member.dto.request.MemberEmailUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberPasswordUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberRegisterRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberUsernameUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.response.MemberUpdateResponseDto;
import com.chokchok.chokchokapi.member.repository.MemberGradeRepository;
import com.chokchok.chokchokapi.member.repository.MemberRepository;
import com.chokchok.chokchokapi.member.repository.MemberRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MemberCommandServiceTest {

    @InjectMocks
    private MemberCommandService memberCommandService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberRoleRepository memberRoleRepository;

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @Mock
    private MemberGradeQueryService memberGradeQueryService;

    @Mock
    private MemberRoleQueryService memberRoleQueryService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공")
    void register_success() {
        // given
        String username = "test";
        String email = "test@chokchok.com";
        String password = "securePassword123!";
        LocalDate dateOfBirth = LocalDate.now();
        Gender gender = Gender.MALE;

        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(username, email, password, dateOfBirth, gender);
        MemberRole memberRole = MemberRole.create("test", "test");
        MemberGrade memberGrade = MemberGrade.create("test", 1, "test");
        Member member = Member.create(memberRole, memberGrade, username, email, password, dateOfBirth, gender);

        // when
        Mockito.when(memberRepository.existsMemberByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(memberRepository.existsMemberByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(memberRoleQueryService.getDefaultMemberRoleEntity()).thenReturn(memberRole);
        Mockito.when(memberGradeQueryService.getDefaultMemberGradeEntity()).thenReturn(memberGrade);
        Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenReturn(member);

        // then
        Assertions.assertDoesNotThrow(() -> memberCommandService.register(requestDto));

        Mockito.verify(memberRepository, Mockito.times(1)).save(Mockito.any(Member.class));
    }

    @Test
    @DisplayName("입력받은 username이 이미 존재하는 경우 예외가 발생한다.")
    void register_fail_whenUsernameDuplicated() {
        // given
        String username = "test";
        String email = "test@chokchok.com";
        String password = "securePassword123!";
        LocalDate dateOfBirth = LocalDate.now();
        Gender gender = Gender.MALE;

        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(username, email, password, dateOfBirth, gender);

        // when
        Mockito.when(memberRepository.existsMemberByUsername(Mockito.anyString())).thenReturn(true);

        // then
        Assertions.assertThrows(ConflictException.class,
                () -> memberCommandService.register(requestDto));

        Mockito.verify(memberRepository, Mockito.times(1)).existsMemberByUsername(Mockito.anyString());
    }

    @Test
    @DisplayName("입력받은 email이 이미 존재하는 경우 예외가 발생한다.")
    void register_fail_whenEmailDuplicated() {
        // given
        String username = "test";
        String email = "test@chokchok.com";
        String password = "securePassword123!";
        LocalDate dateOfBirth = LocalDate.now();
        Gender gender = Gender.MALE;

        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(username, email, password, dateOfBirth, gender);

        // when
        Mockito.when(memberRepository.existsMemberByEmail(Mockito.anyString())).thenReturn(true);

        // then
        Assertions.assertThrows(ConflictException.class,
                () -> memberCommandService.register(requestDto));

        Mockito.verify(memberRepository, Mockito.times(1)).existsMemberByEmail(Mockito.anyString());
    }

    @Test
    @DisplayName("회원 username 수정 성공")
    void updateUsername_success() {
        // given
        String username = "test";
        String email = "test@chokchok.com";
        String password = "securePassword123!";
        LocalDate dateOfBirth = LocalDate.now();
        Gender gender = Gender.MALE;
        MemberRole memberRole = MemberRole.create("test", "test");
        MemberGrade memberGrade = MemberGrade.create("test", 1, "test");

        Member member = Member.create(memberRole, memberGrade, username, email, password, dateOfBirth, gender);
        MemberUsernameUpdateRequestDto requestDto = new MemberUsernameUpdateRequestDto(username);

        Mockito.when(memberRepository.existsMemberByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));

        // when
        MemberUpdateResponseDto result = memberCommandService.updateUsername(Mockito.anyLong(), requestDto);

        // then
        Assertions.assertEquals(username, result.username());
    }

    @Test
    @DisplayName("회원 username 수정 실패 - 존재하지 않는 회원")
    void updateUsername_fail_NotFoundMember() {
        // given
        String username = "test";
        MemberUsernameUpdateRequestDto requestDto = new MemberUsernameUpdateRequestDto(username);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // when then
        Assertions.assertThrows(NotFoundException.class, () -> memberCommandService.updateUsername(Mockito.anyLong(), requestDto));

        Mockito.verify(memberRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("회원 username 수정 실패 - 중복된 username")
    void updateUsername_fail_AlreadyExistsUsername() {
        // given
        String username = "test";
        String email = "test@chokchok.com";
        String password = "securePassword123!";
        LocalDate dateOfBirth = LocalDate.now();
        Gender gender = Gender.MALE;
        MemberRole memberRole = MemberRole.create("test", "test");
        MemberGrade memberGrade = MemberGrade.create("test", 1, "test");

        Member member = Member.create(memberRole, memberGrade, username, email, password, dateOfBirth, gender);
        MemberUsernameUpdateRequestDto requestDto = new MemberUsernameUpdateRequestDto(username);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
        Mockito.when(memberRepository.existsMemberByUsername(Mockito.anyString())).thenReturn(true);

        // when then
        Assertions.assertThrows(ConflictException.class, () -> memberCommandService.updateUsername(Mockito.anyLong(), requestDto));

        Mockito.verify(memberRepository, Mockito.times(1)).existsMemberByUsername(Mockito.anyString());
    }

    @Test
    @DisplayName("회원 이메일 수정 성공")
    void updateEmail_success() {
        // given
        String username = "test";
        String email = "test@chokchok.com";
        String password = "securePassword123!";
        LocalDate dateOfBirth = LocalDate.now();
        Gender gender = Gender.MALE;
        MemberRole memberRole = MemberRole.create("test", "test");
        MemberGrade memberGrade = MemberGrade.create("test", 1, "test");

        Member member = Member.create(memberRole, memberGrade, username, email, password, dateOfBirth, gender);
        MemberEmailUpdateRequestDto requestDto = new MemberEmailUpdateRequestDto(email);

        Mockito.when(memberRepository.existsMemberByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));

        // when
        MemberUpdateResponseDto result = memberCommandService.updateEmail(Mockito.anyLong(), requestDto);

        // then
        Assertions.assertEquals(email, result.email());
    }

    @Test
    @DisplayName("회원 이메일 수정 실패 - 존재하지 않는 회원")
    void updateEmail_fail_NotFoundMember() {
        // given
        String email = "test@chokchok.com";
        MemberEmailUpdateRequestDto requestDto = new MemberEmailUpdateRequestDto(email);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // when then
        Assertions.assertThrows(NotFoundException.class, () -> memberCommandService.updateEmail(Mockito.anyLong(), requestDto));

        Mockito.verify(memberRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("회원 이메일 수정 실패 - 중복된 이메일")
    void updateEmail_fail_AlreadyExistsEmail() {
        // given
        String username = "test";
        String email = "test@chokchok.com";
        String password = "securePassword123!";
        LocalDate dateOfBirth = LocalDate.now();
        Gender gender = Gender.MALE;
        MemberRole memberRole = MemberRole.create("test", "test");
        MemberGrade memberGrade = MemberGrade.create("test", 1, "test");

        Member member = Member.create(memberRole, memberGrade, username, email, password, dateOfBirth, gender);
        MemberEmailUpdateRequestDto requestDto = new MemberEmailUpdateRequestDto(username);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
        Mockito.when(memberRepository.existsMemberByEmail(Mockito.anyString())).thenReturn(true);

        // when then
        Assertions.assertThrows(ConflictException.class, () -> memberCommandService.updateEmail(Mockito.anyLong(), requestDto));

        Mockito.verify(memberRepository, Mockito.times(1)).existsMemberByEmail(Mockito.anyString());
    }

    @Test
    @DisplayName("회원 비밀번호 수정 성공")
    void updatePassword_success() {
        // given
        String username = "test";
        String email = "test@chokchok.com";
        String password = "securePassword123!";
        LocalDate dateOfBirth = LocalDate.now();
        Gender gender = Gender.MALE;
        MemberRole memberRole = MemberRole.create("test", "test");
        MemberGrade memberGrade = MemberGrade.create("test", 1, "test");

        Member member = Member.create(memberRole, memberGrade, username, email, password, dateOfBirth, gender);
        MemberPasswordUpdateRequestDto requestDto = new MemberPasswordUpdateRequestDto(email);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));

        // when then
        Assertions.assertDoesNotThrow(() -> memberCommandService.updatePassword(Mockito.anyLong(), requestDto));
    }

    @Test
    @DisplayName("회원 비밀번호 수정 실패 - 존재하지 않는 회원")
    void updatePassword_fail_NotFoundMember() {
        // given
        String password = "securePassword123!";
        MemberPasswordUpdateRequestDto requestDto = new MemberPasswordUpdateRequestDto(password);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // when then
        Assertions.assertThrows(NotFoundException.class, () -> memberCommandService.updatePassword(Mockito.anyLong(), requestDto));

        Mockito.verify(memberRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void deleteMember_success() {
        // given
        String username = "test";
        String email = "test@chokchok.com";
        String password = "securePassword123!";
        LocalDate dateOfBirth = LocalDate.now();
        Gender gender = Gender.MALE;
        MemberRole memberRole = MemberRole.create("test", "test");
        MemberGrade memberGrade = MemberGrade.create("test", 1, "test");

        Member member = Member.create(memberRole, memberGrade, username, email, password, dateOfBirth, gender);

        Mockito.when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));

        // when then
        Assertions.assertDoesNotThrow(() -> memberCommandService.withdraw(Mockito.anyLong()));
    }





}