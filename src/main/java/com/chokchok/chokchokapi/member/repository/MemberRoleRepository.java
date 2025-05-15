package com.chokchok.chokchokapi.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.chokchok.chokchokapi.member.domain.MemberRole;

import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Integer> {
    Optional<MemberRole> findByName(String name);
}
