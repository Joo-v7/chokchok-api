package com.chokchok.chokchokapi.member.repository;

import org.springframework.data.repository.CrudRepository;
import com.chokchok.chokchokapi.member.domain.MemberRole;

import java.util.Optional;

public interface MemberRoleRepository extends CrudRepository<MemberRole, Integer> {
    Optional<MemberRole> findByName(String name);
}
