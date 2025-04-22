package role.repository;

import org.springframework.data.repository.CrudRepository;
import role.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
