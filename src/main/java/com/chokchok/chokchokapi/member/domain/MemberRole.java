package com.chokchok.chokchokapi.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * id | name  | authority
 * 1  | user  | ROLE_USER   // 회원
 * 2  | admin | ROLE_ADMIN  // 관리자
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name="member_roles")
@Entity
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 30)
    private String authority;

    private MemberRole(String name, String authority) {
        this.name = name;
        this.authority = authority;
    }

    public static MemberRole create(String name, String authority) {
        return new MemberRole(name, authority);
    }

}