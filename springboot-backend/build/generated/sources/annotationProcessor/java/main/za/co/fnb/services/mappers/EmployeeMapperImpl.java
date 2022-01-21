package za.co.fnb.services.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import za.co.fnb.domain.Employee;
import za.co.fnb.domain.dto.EmployeeDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-01-20T12:36:59+0200",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-6.7.1.jar, environment: Java 11.0.13 (Ubuntu)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public Employee toEntity(EmployeeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setId( dto.getId() );
        employee.setFirstName( dto.getFirstName() );
        employee.setMiddleName( dto.getMiddleName() );
        employee.setLastName( dto.getLastName() );
        employee.setDateOfJoining( dto.getDateOfJoining() );
        employee.setDateOfExit( dto.getDateOfExit() );
        employee.setStatus( dto.getStatus() );

        return employee;
    }

    @Override
    public List<Employee> toEntity(List<EmployeeDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Employee> list = new ArrayList<Employee>( dtoList.size() );
        for ( EmployeeDTO employeeDTO : dtoList ) {
            list.add( toEntity( employeeDTO ) );
        }

        return list;
    }

    @Override
    public List<EmployeeDTO> toDto(List<Employee> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<EmployeeDTO> list = new ArrayList<EmployeeDTO>( entityList.size() );
        for ( Employee employee : entityList ) {
            list.add( toDto( employee ) );
        }

        return list;
    }

    @Override
    public void partialUpdate(Employee entity, EmployeeDTO dto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getFirstName() != null ) {
            entity.setFirstName( dto.getFirstName() );
        }
        if ( dto.getMiddleName() != null ) {
            entity.setMiddleName( dto.getMiddleName() );
        }
        if ( dto.getLastName() != null ) {
            entity.setLastName( dto.getLastName() );
        }
        if ( dto.getDateOfJoining() != null ) {
            entity.setDateOfJoining( dto.getDateOfJoining() );
        }
        if ( dto.getDateOfExit() != null ) {
            entity.setDateOfExit( dto.getDateOfExit() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
    }

    @Override
    public EmployeeDTO toDto(Employee s) {
        if ( s == null ) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId( s.getId() );
        employeeDTO.setFirstName( s.getFirstName() );
        employeeDTO.setMiddleName( s.getMiddleName() );
        employeeDTO.setLastName( s.getLastName() );
        employeeDTO.setDateOfJoining( s.getDateOfJoining() );
        employeeDTO.setDateOfExit( s.getDateOfExit() );
        employeeDTO.setStatus( s.getStatus() );

        return employeeDTO;
    }

    @Override
    public EmployeeDTO toDtoId(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId( employee.getId() );

        return employeeDTO;
    }
}
