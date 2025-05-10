package com.chokchok.chokchokapi.member.service;

import com.chokchok.chokchokapi.common.exception.base.ConflictException;
import com.chokchok.chokchokapi.common.exception.base.NotFoundException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.chokchok.chokchokapi.member.domain.Member;
import com.chokchok.chokchokapi.member.domain.MemberGrade;
import com.chokchok.chokchokapi.member.domain.MemberRole;
import com.chokchok.chokchokapi.member.domain.Status;
import com.chokchok.chokchokapi.member.dto.request.MemberEmailUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberPasswordUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberRegisterRequestDto;
import com.chokchok.chokchokapi.member.dto.request.MemberUsernameUpdateRequestDto;
import com.chokchok.chokchokapi.member.dto.response.MemberRegisterResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberStatusResponseDto;
import com.chokchok.chokchokapi.member.dto.response.MemberUpdateResponseDto;
import com.chokchok.chokchokapi.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 등록/수정/삭제를 담당하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleService memberRoleService;
    private final MemberGradeService memberGradeService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 등록(회원 가입)
     * @param memberRegisterRequestDto
     * @return MemberRegisterResponseDto
     */
    @Transactional
    public MemberRegisterResponseDto register(MemberRegisterRequestDto memberRegisterRequestDto) {
        // username, email 중복 체크
        checkDuplication(memberRegisterRequestDto.username(), memberRegisterRequestDto.email());

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(memberRegisterRequestDto.password());

        // Default MemberRole, MemberGrade
        MemberRole memberRole = memberRoleService.getDefaultMemberRoleEntity();
        MemberGrade memberGrade = memberGradeService.getDefaultMemberGradeEntity();

        // Member Entity 생성
        Member member = Member.create(
                memberRole,
                memberGrade,
                memberRegisterRequestDto.username(),
                memberRegisterRequestDto.email(),
                encodedPassword,
                memberRegisterRequestDto.dateOfBirth(),
                memberRegisterRequestDto.gender()
        );

        try {
            Member savedMember = memberRepository.save(member);
            return MemberRegisterResponseDto.from(savedMember);
        } catch (DataIntegrityViolationException e) {
            log.error("회원 등록 중 데이터 무결성 위반 발생: {}", e.getMessage());
            throw new ConflictException(ErrorCode.MEMBER_ALREADY_EXISTS, "이미 존재하는 회원입니다.");
        } catch(Exception e) {
            log.error("회원가입 중 알 수 없는 오류 발생",e);
            throw new RuntimeException("회원가입 중 알 수 없는 오류가 발생했습니다.");
        }

    }

    /**
     * 회원가입 시 중복 사항을 체크하는 메소드
     * @param username 조회 대상
     * @param email 조회 대상
     * @throws ConflictException username, email 이 기존에 있다면 발생하는 예외
     */
    private void checkDuplication(String username, String email) {
        if (memberRepository.existsMemberByUsername(username)) {
            throw new ConflictException(ErrorCode.MEMBER_NAME_ALREADY_EXISTS, "이미 사용중인 사용자 이름입니다.");
        }

        if (memberRepository.existsMemberByEmail(email)) {
            throw new ConflictException(ErrorCode.MEMBER_EMAIL_ALREADY_EXISTS, "이미 사용중인 이메일입니다.");
        }
    }

    /**
     * 회원정보 수정 - username
     * @param memberId
     * @param memberUsernameUpdateRequestDto
     * @return MemberUpdateResponseDto
     */
    @Transactional
    public MemberUpdateResponseDto updateUsername(
            Long memberId,
            MemberUsernameUpdateRequestDto memberUsernameUpdateRequestDto
    ) {
        String username = memberUsernameUpdateRequestDto.username();

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, "Member Not Found with: " + memberId)
        );

        checkUsernameDuplication(username);

        member.updateUsername(username);

        return MemberUpdateResponseDto.from(member);
    }

    private void checkUsernameDuplication(String username) {
        if(memberRepository.existsMemberByUsername(username)) {
           throw new ConflictException(ErrorCode.MEMBER_NAME_ALREADY_EXISTS, "이미 사용중인 사용자 이름입니다.");
        }
    }

    /**
     * 회원정보 수정 - email
     * @param memberId
     * @param memberEmailUpdateRequestDto
     * @return MemberUpdateResponseDto
     */
    @Transactional
    public MemberUpdateResponseDto updateEmail(
            Long memberId,
            MemberEmailUpdateRequestDto memberEmailUpdateRequestDto
    ) {
        String email = memberEmailUpdateRequestDto.email();

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, "Member Not Found with: " + memberId)
        );

        checkEmailDuplication(email);

        member.updateEmail(email);

        return MemberUpdateResponseDto.from(member);
    }

    private void checkEmailDuplication(String email) {
        if(memberRepository.existsMemberByEmail(email)) {
            throw new ConflictException(ErrorCode.MEMBER_EMAIL_ALREADY_EXISTS, "이미 사용중인 이메일입니다.");
        }
    }

    /**
     * 회원정보 수정 - password
     * @param memberId
     * @param memberPasswordUpdateRequestDto
     * @return MemberUpdateResponseDto
     */
    @Transactional
    public MemberUpdateResponseDto updatePassword(
            Long memberId,
            MemberPasswordUpdateRequestDto memberPasswordUpdateRequestDto
    ) {
        String password = memberPasswordUpdateRequestDto.password();
        String encodedPassword = passwordEncoder.encode(password);

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, "Member Not Found with id: " + memberId)
        );

        member.updatePassword(encodedPassword);

        return MemberUpdateResponseDto.from(member);
    }

    /**
     * 회원 삭제 - 회원 상태 DELETED 로 변경
     * @param memberId
     * @return MemberStatusResponseDto
     */
    @Transactional
    public MemberStatusResponseDto withdraw(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, "Member Not Found with id: " + memberId)
        );

        member.updateStatus(Status.DELETED);

        return MemberStatusResponseDto.from(member);
    }

}
