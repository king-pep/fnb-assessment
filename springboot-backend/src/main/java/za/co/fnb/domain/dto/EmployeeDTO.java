package za.co.fnb.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EmployeeDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    private String firstName;
    @NotNull
    @JsonProperty("middleName")
    private String middleName;
    @NotNull
    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("dateOfJoining")
    private Date dateOfJoining;

    @JsonProperty("dateOfExit")
    private Date dateOfExit;
    @JsonProperty("status")
    private String status;
}
