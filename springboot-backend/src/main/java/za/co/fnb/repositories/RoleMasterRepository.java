package za.co.fnb.repositories;

import org.springframework.stereotype.Repository;
import za.co.fnb.domain.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RoleMasterRepository extends JpaRepository<RoleMaster, Long> {}
