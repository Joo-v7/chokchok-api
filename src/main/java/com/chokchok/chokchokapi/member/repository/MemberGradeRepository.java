package com.chokchok.chokchokapi.member.repository;

import com.chokchok.chokchokapi.member.domain.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Integer> {
    Optional<MemberGrade> findByName(String name);
}
