package za.co.fnb.web.controllers;

import za.co.fnb.domain.EmployeeRoleMapping;
import za.co.fnb.services.EmployeeRoleMappingService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee-role-mapping")
@Slf4j
public class EmployeeRoleMappingController {

    private final EmployeeRoleMappingService employeeRoleMappingService;

    @Autowired
    public EmployeeRoleMappingController(EmployeeRoleMappingService employeeRoleMappingService) {
        this.employeeRoleMappingService = employeeRoleMappingService;
    }

    @GetMapping
    public List<EmployeeRoleMapping> getAllEmployeeRoleMappings() {
        return employeeRoleMappingService.findAllEmployeeRoleMappings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRoleMapping> getEmployeeRoleMappingById(@PathVariable Long id) {
        return employeeRoleMappingService
                .findEmployeeRoleMappingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeRoleMapping createEmployeeRoleMapping(@RequestBody @Validated EmployeeRoleMapping employeeRoleMapping) {
        return employeeRoleMappingService.saveEmployeeRoleMapping(employeeRoleMapping);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeRoleMapping> updateEmployeeRoleMapping(
            @PathVariable Long id, @RequestBody EmployeeRoleMapping employeeRoleMapping) {
        return employeeRoleMappingService
                .findEmployeeRoleMappingById(id)
                .map(
                        employeeRoleMappingObj -> {
                            employeeRoleMapping.setId(id);
                            return ResponseEntity.ok(employeeRoleMappingService.saveEmployeeRoleMapping(employeeRoleMapping));
                        })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeRoleMapping> deleteEmployeeRoleMapping(@PathVariable Long id) {
        return employeeRoleMappingService
                .findEmployeeRoleMappingById(id)
                .map(
                        employeeRoleMapping -> {
                            employeeRoleMappingService.deleteEmployeeRoleMappingById(id);
                            return ResponseEntity.ok(employeeRoleMapping);
                        })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
