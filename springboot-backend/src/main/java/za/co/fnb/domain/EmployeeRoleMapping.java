package za.co.fnb.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "employee_role_mappings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRoleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeRoleMapping_id_generator")
    @SequenceGenerator(
        name = "employeeRoleMapping_id_generator",
        sequenceName = "employeeRoleMapping_id_seq",
        allocationSize = 100)
    private Long id;

    @Column(nullable = false)
    private Date effectiveDate;

    @OneToOne
    @JoinColumn(unique = true, name = "EmployeeId")
    private Employee employee;
    @OneToOne
    @JoinColumn(unique = true, name = "roleId")
    private RoleMaster roleMaster;
}
