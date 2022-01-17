package za.co.fnb.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.fnb.domain.EmployeeRoleMapping;
import za.co.fnb.repositories.EmployeeRoleMappingRepository;
import za.co.fnb.services.EmployeeRoleMappingService;

import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class EmployeeRoleMappingServiceImpl implements EmployeeRoleMappingService {

    private final EmployeeRoleMappingRepository employeeRoleMappingRepository;

    @Autowired
    public EmployeeRoleMappingServiceImpl(EmployeeRoleMappingRepository employeeRoleMappingRepository) {
        this.employeeRoleMappingRepository = employeeRoleMappingRepository;
    }

    public List<EmployeeRoleMapping> findAllEmployeeRoleMappings() {
        return employeeRoleMappingRepository.findAll();
    }

    public Optional<EmployeeRoleMapping> findEmployeeRoleMappingById(Long id) {
        return employeeRoleMappingRepository.findById(id);
    }

    public EmployeeRoleMapping saveEmployeeRoleMapping(EmployeeRoleMapping employeeRoleMapping) {
        return employeeRoleMappingRepository.save(employeeRoleMapping);
    }

    public void deleteEmployeeRoleMappingById(Long id) {
        employeeRoleMappingRepository.deleteById(id);
    }
}
