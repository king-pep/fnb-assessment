package za.co.fnb.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_id_generator")
    @SequenceGenerator(
        name = "employee_id_generator",
        sequenceName = "employee_id_seq",
        allocationSize = 100)
    private Long id;
    @Column(name = "FirstName")
    private String firstName;
    @Column(nullable = false, name = "MiddleName")
   private String middleName;
    @Column(nullable = false, name = "LastName")
    private String lastName;
    @Column(nullable = false, name = "DateOfJoining")
    private Date dateOfJoining;
    @Column(name="DateOfExit")
    private Date dateOfExit;
    @Column(nullable = false, name = "status")
   private String status;
}
