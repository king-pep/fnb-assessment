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

@Entity
@Table(name = "role_masters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleMaster_id_generator")
    @SequenceGenerator(
        name = "roleMaster_id_generator",
        sequenceName = "roleMaster_id_seq",
        allocationSize = 100)
    private Long id;

    @Column(nullable = false, name = "RoleName")
    @NotEmpty(message = "role name cannot be empty")
    private String roleName;
    @Column(nullable = false, name = "status")
    @NotEmpty(message = "status of joining can not cannot be empty")
    private String status;
}
