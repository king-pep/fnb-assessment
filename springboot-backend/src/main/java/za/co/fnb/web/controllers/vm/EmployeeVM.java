package za.co.fnb.web.controllers.vm;

import lombok.Getter;
import lombok.Setter;
import za.co.fnb.domain.dto.EmployeeDTO;
@Getter
@Setter
public class EmployeeVM extends EmployeeDTO {

    private String role;


    public EmployeeVM employeeVMToEmployeeDTO(EmployeeDTO employee){
        if ( employee == null ) {
            return null;
        }
        this.setId( employee.getId() );
        this.setFirstName( employee.getFirstName() );
        this.setMiddleName( employee.getMiddleName() );
        this.setLastName( employee.getLastName() );
        this.setDateOfJoining( employee.getDateOfJoining() );
        this.setDateOfExit( employee.getDateOfExit() );
        this.setStatus( employee.getStatus() );

         return this;
    }
}
