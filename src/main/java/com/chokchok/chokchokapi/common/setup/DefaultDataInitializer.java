package com.chokchok.chokchokapi.common.setup;

import com.chokchok.chokchokapi.member.domain.MemberGrade;
import com.chokchok.chokchokapi.member.domain.MemberRole;
import com.chokchok.chokchokapi.member.repository.MemberGradeRepository;
import com.chokchok.chokchokapi.member.repository.MemberRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 어플리케이션 운영에 필수적인 초기 데이터를 삽입하는 클래스입니다.
 * 어플리케이션 시작 시 @PostConstruct를 통해 실행됩니다.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultDataInitializer {

    private final MemberRoleRepository memberRoleRepository;
    private final MemberGradeRepository memberGradeRepository;

    /**
     * 초기 데이터를 삽입하는 메서드입니다.
     * 트랜잭션으로 관리됩니다.
     */
    @Transactional
    @PostConstruct
    public void initData() {
        log.info("초기 데이터 삽입 시작");
        try {

            initRoles();
            initGrades();

        } catch (Exception e) {
            log.error("초기 데이터 삽입 중 오류 발생: ", e);
            throw new RuntimeException("초기 데이터 삽입 실패", e);
        }
    }

    /**
     * 초기 권한(Role) 데이터를 삽입합니다.
     */
    private void initRoles() {
        if (memberRoleRepository.count() == 0) {
            log.info("초기 권한 데이터 삽입 시작");

            memberRoleRepository.save(MemberRole.create("user", "ROLE_USER"));
            memberRoleRepository.save(MemberRole.create("admin", "ROLE_ADMIN"));

            log.info("초기 권한 데이터 삽입 완료");
        } else {
            log.info("권한 데이터가 이미 존재합니다.");
        }
    }

    /**
     * 초기 등급(Grade) 데이터를 삽입합니다.
     */
    private void initGrades() {
        if (memberGradeRepository.count() == 0) {
            log.info("초기 등급 데이터 삽입 시작");

            memberGradeRepository.save(MemberGrade.create("BASIC", 1, "결제 시 순수금액의 1% 포인트 정립"));
            memberGradeRepository.save(MemberGrade.create("SILVER", 3, "결제 시 순수금액의 3% 포인트 정립"));
            memberGradeRepository.save(MemberGrade.create("GOLD", 5, "결제 시 순수금액의 5% 포인트 정립"));
            memberGradeRepository.save(MemberGrade.create("VIP", 7, "결제 시 순수금액의 7% 포인트 정립"));

            log.info("초기 등급 데이터 삽입 완료");
        } else {
            log.info("등급 데이터가 이미 존재합니다.");
        }
    }

}
