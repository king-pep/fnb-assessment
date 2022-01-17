package za.co.fnb.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import za.co.fnb.domain.Employee;
import za.co.fnb.domain.EmployeeRoleMapping;
import za.co.fnb.domain.dto.EmployeeDTO;
import za.co.fnb.domain.enums.RoleType;
import za.co.fnb.domain.enums.StatusType;
import za.co.fnb.services.EmployeeRoleMappingService;
import za.co.fnb.services.EmployeeService;
import za.co.fnb.web.controllers.vm.EmployeeVM;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/api/employees")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    EmployeeRoleMappingService employeeRoleMappingService;
    private List<EmployeeVM> employeeList;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeRoleMappingService employeeRoleMappingService) {
        this.employeeService = employeeService;
         this.employeeRoleMappingService = employeeRoleMappingService;
    }

    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {

        return employeeService.findAll();
    }
    @GetMapping("/employees-with-roles")
    public List<EmployeeVM> getAllEmployeeWithRole() {
  employeeList = new ArrayList<>();
     employeeRoleMappingService.findAllEmployeeRoleMappings().forEach(map ->{
         EmployeeVM vm= new EmployeeVM();
         EmployeeDTO dto = employeeService.findEmployeeById(map
             .getEmployee().getId()).get();
         vm = vm.employeeVMToEmployeeDTO(dto);
         vm.setRole(map.getRoleMaster().getRoleName());
          employeeList.add(vm);

     });

        return employeeList;
    }


    @GetMapping("/paginated")
    public Page<EmployeeDTO> list(@RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EmployeeDTO> pageResult = employeeService.findAll(pageRequest);
        List<EmployeeDTO> todos = pageResult.getContent();

        return new PageImpl<>(todos, pageRequest, pageResult.getTotalElements());

    }


    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return employeeService
                .findEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO createEmployee(@RequestBody @Validated EmployeeVM employee) {
        int status = 0;
        employee.setStatus(StatusType.ACTIVE.toString());

        String roleName = (employee.getRole().equalsIgnoreCase(RoleType.SENIOR_DEVELOPER.toString()))?
            RoleType.SENIOR_DEVELOPER.toString(): (employee.getRole().equalsIgnoreCase(RoleType.INTERMEDIATE_DEVELOPER.toString()))?
            RoleType.INTERMEDIATE_DEVELOPER.toString(): RoleType.JUNIOR_DEVELOPER.toString();
            employee.setRole(roleName);
        employee.setDateOfJoining(new Date());

        return employeeService.save(employee, employee.getRole());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id, @RequestBody EmployeeVM employee) {

        if(employee.getDateOfExit() != null){
            if(employee.getDateOfExit().before(employee.getDateOfJoining()))
                throw new IllegalArgumentException("DATE OF EXIT CAN NOT BE BEFORE DATE JOINED");
        }


        if(!employee.getStatus().equalsIgnoreCase(StatusType.ACTIVE.toString())){
            throw new IllegalArgumentException("ONLY ACTIVE CAN BE ASSIGNED TO AN EMPLOYEE");

        }

        return employeeService
                .findEmployeeById(id)
                .map(
                        employeeObj -> {
                            employee.setId(id);
                            return ResponseEntity.ok(employeeService.save(employee, employee.getRole()));
                        })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeDTO> deleteEmployee(@PathVariable Long id) {
        return employeeService
                .findEmployeeById(id)
                .map(
                        employee -> {
                            employeeService.delete(id);
                            return ResponseEntity.ok(employee);
                        })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
