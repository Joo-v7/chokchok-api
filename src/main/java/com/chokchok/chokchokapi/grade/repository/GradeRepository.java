package com.chokchok.chokchokapi.grade.repository;

import com.chokchok.chokchokapi.grade.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
}
