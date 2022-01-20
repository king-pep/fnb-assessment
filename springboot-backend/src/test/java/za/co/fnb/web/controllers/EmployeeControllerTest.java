package za.co.fnb.web.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.fnb.domain.Employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;
import za.co.fnb.domain.dto.EmployeeDTO;
import za.co.fnb.services.EmployeeService;

@WebMvcTest(controllers = EmployeeController.class)
@ActiveProfiles("test")
class EmployeeControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private EmployeeService employeeService;

    @Autowired private ObjectMapper objectMapper;

    private List<EmployeeDTO> employeeList;

    @BeforeEach
    void setUp() {
        this.employeeList = new ArrayList<>();

        employeeList.add(new EmployeeDTO(1L, "FirstEmployeeFirstName","FirstEmployeeMiddleName",
            "FirstEmployeeLastName",new Date(),new Date(),"active"));
        employeeList.add(new EmployeeDTO(2L, "SecondEmployeeFirstName","SecondEmployeeMiddleName",
            "SecondEmployeeLastName",new Date(),new Date(),"active"));
        employeeList.add(new EmployeeDTO(3L, "ThirdEmployeeFirstName","ThirdEmployeeMiddleName",
            "ThirdEmployeeLastName",new Date(),new Date(),"active"));

        objectMapper.registerModule(new ProblemModule());
        objectMapper.registerModule(new ConstraintViolationProblemModule());
    }

    @Test
    void shouldFetchAllEmployees() throws Exception {
        given(employeeService.findAll()).willReturn(this.employeeList);
        this.mockMvc
                .perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));
    }

    @Test
    void shouldFindEmployeeById() throws Exception {
        Long employeeId = 1L;
        EmployeeDTO employee = new EmployeeDTO(1L, "FirstEmployeeFirstName","FirstEmployeeMiddleName",
            "FirstEmployeeLastName",new Date(),new Date(),"active");
        given(employeeService.findEmployeeById(employeeId)).willReturn(Optional.of(employee));

        this.mockMvc
                .perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(employee.getFirstName())));
    }

    @Test
    void shouldReturn404WhenFetchingNonExistingEmployee() throws Exception {
        Long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId)).willReturn(Optional.empty());

        this.mockMvc
                .perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewEmployee() throws Exception {
        given(employeeService.save(any(EmployeeDTO.class), "active"))
                .willAnswer((invocation) -> invocation.getArgument(0));

        EmployeeDTO employee = new EmployeeDTO(1L, "FirstEmployeeFirstName","FirstEmployeeMiddleName",
            "FirstEmployeeLastName",new Date(),new Date(),"active");        this.mockMvc
                .perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.FirstName", is(employee.getFirstName())));
    }

    @Test
    void shouldReturn400WhenCreateNewEmployeeWithoutLastName() throws Exception {
        EmployeeDTO employee = new EmployeeDTO(1L, "FirstEmployeeFirstName","FirstEmployeeMiddleName",
            null,new Date(),new Date(),"active");

        this.mockMvc
                .perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/problem+json")))
                .andExpect(
                        jsonPath(
                                "$.type",
                                is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", is("text")))
                .andExpect(jsonPath("$.violations[0].message", is("Text cannot be empty")))
                .andReturn();
    }

    @Test
    void shouldUpdateEmployee() throws Exception {
        Long employeeId = 1L;
        EmployeeDTO employee = new EmployeeDTO(1L, "FirstEmployeeFirstNamde","FirdstEmployeeMiddleName",
            "FirstEmpdloyeeLastName",new Date(),new Date(),"active");
        given(employeeService.findEmployeeById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.save(any(EmployeeDTO.class),"active"))
                .willAnswer((invocation) -> invocation.getArgument(0));

        this.mockMvc
                .perform(
                        put("/api/employees/{id}", employee.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingEmployee() throws Exception {
        Long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId)).willReturn(Optional.empty());
        EmployeeDTO employee = new EmployeeDTO(1L, "FirstEmployeeFirstName","FirstEmployeeMiddleName",
            "FirstEmployeeLastName",new Date(),new Date(),"active");

        this.mockMvc
                .perform(
                        put("/api/employees/{id}", employeeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        Long employeeId = 1L;
        EmployeeDTO employee = new EmployeeDTO(1L, "FirstEmployeeFirstName","FirstEmployeeMiddleName",
            "FirstEmployeeLastName",new Date(),new Date(),"active");
        given(employeeService.findEmployeeById(employeeId)).willReturn(Optional.of(employee));
        doNothing().when(employeeService).delete(employee.getId());

        this.mockMvc
                .perform(delete("/api/employees/{id}", employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())));
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingEmployee() throws Exception {
        Long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId)).willReturn(Optional.empty());

        this.mockMvc
                .perform(delete("/api/employees/{id}", employeeId))
                .andExpect(status().isNotFound());
    }
}
