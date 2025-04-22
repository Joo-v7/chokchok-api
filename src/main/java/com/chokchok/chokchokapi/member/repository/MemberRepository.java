package com.chokchok.chokchokapi.member.repository;

import com.chokchok.chokchokapi.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
