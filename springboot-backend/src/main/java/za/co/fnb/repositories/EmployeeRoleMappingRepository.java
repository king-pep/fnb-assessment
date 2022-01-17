package za.co.fnb.repositories;

import org.springframework.stereotype.Repository;
import za.co.fnb.domain.EmployeeRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface EmployeeRoleMappingRepository extends JpaRepository<EmployeeRoleMapping, Long> {}
