package com.chokchok.chokchokapi.member.domain;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "members")
@EntityListeners(AuditingEntityListener.class) // Auditing 활성화 (CreatedDate 적용)
public class Member {

    @Id
    @Tsid
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private MemberRole memberRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private MemberGrade memberGrade;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastLoginAt;

    private Member(MemberRole memberRole, MemberGrade memberGrade, String email, String password, String username, LocalDate dateOfBirth, Gender gender) {
        this.memberRole = memberRole;
        this.memberGrade = memberGrade;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = Status.ACTIVE;
    }

    /**
     * 회원을 생성하는 정적 팰토리 메소드
     * @return Member
     */
    public static Member create(MemberRole memberRole, MemberGrade memberGrade, String email, String password, String username, LocalDate dateOfBirth, Gender gender) {
        Member member = new Member(memberRole, memberGrade, email, password, username, dateOfBirth, gender);
        return member;
    }

    /**
     * 회원의 사용자명을 업데이트하는 메소드
     * @param username
     */
    public void updateUsername(String username) {
        this.username = username;
    }

    /**
     * 회원의 이메일을 업데이트하는 메소드
     * @param email
     */
    public void updateEmail(String email) {
        this.email = email;
    }

    /**
     * 회원의 비밀번호를 업데이트하는 메소드
     * @param password
     */
    public void updatePassword(String password) {
        this.password = password;
    }

    /**
     * 회원의 상태를 업데이트하는 메소드
     * @param status
     */
    public void updateStatus(Status status) {
        this.status = status;
    }


}
