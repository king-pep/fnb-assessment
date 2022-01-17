package za.co.fnb.services;

import za.co.fnb.domain.EmployeeRoleMapping;
import za.co.fnb.repositories.EmployeeRoleMappingRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface EmployeeRoleMappingService {



     List<EmployeeRoleMapping> findAllEmployeeRoleMappings();

     Optional<EmployeeRoleMapping> findEmployeeRoleMappingById(Long id);

     EmployeeRoleMapping saveEmployeeRoleMapping(EmployeeRoleMapping employeeRoleMapping);

     void deleteEmployeeRoleMappingById(Long id);

}
