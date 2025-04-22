package com.chokchok.chokchokapi.role.repository;

import org.springframework.data.repository.CrudRepository;
import com.chokchok.chokchokapi.role.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
