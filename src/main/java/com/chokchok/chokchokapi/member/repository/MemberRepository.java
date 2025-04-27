package com.chokchok.chokchokapi.member.repository;

import com.chokchok.chokchokapi.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsMemberByUsername(String username);
    boolean existsMemberByEmail(String email);
    Optional<Member> findMemberByEmail(String email);
}
