package za.co.fnb.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.fnb.domain.Employee;
import za.co.fnb.domain.EmployeeRoleMapping;
import za.co.fnb.domain.RoleMaster;
import za.co.fnb.repositories.EmployeeRepository;
import za.co.fnb.repositories.EmployeeRoleMappingRepository;
import za.co.fnb.repositories.RoleMasterRepository;
import za.co.fnb.services.EmployeeService;
import za.co.fnb.services.mappers.EmployeeMapper;
import za.co.fnb.domain.dto.EmployeeDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeRoleMappingRepository employeeRoleMappingRepository;
    private final RoleMasterRepository roleMasterRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
                               EmployeeRoleMappingRepository employeeRoleMappingRepository,
                               RoleMasterRepository roleMasterRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.employeeRoleMappingRepository = employeeRoleMappingRepository;
        this.roleMasterRepository = roleMasterRepository;
    }

    @Override
    public EmployeeDTO save(EmployeeDTO employeeDTO, String role) {
        log.debug("Request to save Employee : {}", employeeDTO);

        RoleMaster  roleMaster = new RoleMaster();
        EmployeeRoleMapping employeeRoleMapping = new EmployeeRoleMapping();
        employeeRoleMapping.setEffectiveDate(new Date());
        roleMaster.setRoleName(role);
        roleMaster.setStatus(employeeDTO.getStatus());
        roleMasterRepository.save(roleMaster);

        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee = employeeRepository.save(employee);

        employeeRoleMapping.setEmployee(employee);
        employeeRoleMapping.setRoleMaster(roleMaster);
        employeeRoleMappingRepository.save(employeeRoleMapping);
        return employeeMapper.toDto(employee);
    }

    @Override
    public Optional<EmployeeDTO> partialUpdate(EmployeeDTO employeeDTO) {
        log.debug("Request to partially update Employee : {}", employeeDTO);

        return employeeRepository
            .findById(employeeDTO.getId())
            .map(existingEmployee -> {
                employeeMapper.partialUpdate(existingEmployee, employeeDTO);

                return existingEmployee;
            })
            .map(employeeRepository::save)
            .map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(pageable).map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findAll() {

        return employeeRepository.findAll()
            .stream().map(employeeMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> findEmployeeById(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id).map(employeeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }
}
