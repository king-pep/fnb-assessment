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
import za.co.fnb.domain.EmployeeRoleMapping;
import za.co.fnb.services.EmployeeRoleMappingService;
import java.util.ArrayList;
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

@WebMvcTest(controllers = EmployeeRoleMappingController.class)
@ActiveProfiles("test")
class EmployeeRoleMappingControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private EmployeeRoleMappingService employeeRoleMappingService;

    @Autowired private ObjectMapper objectMapper;

    private List<EmployeeRoleMapping> employeeRoleMappingList;

    @BeforeEach
    void setUp() {
        this.employeeRoleMappingList = new ArrayList<>();
        this.employeeRoleMappingList.add(new EmployeeRoleMapping(1L, "text 1"));
        this.employeeRoleMappingList.add(new EmployeeRoleMapping(2L, "text 2"));
        this.employeeRoleMappingList.add(new EmployeeRoleMapping(3L, "text 3"));

        objectMapper.registerModule(new ProblemModule());
        objectMapper.registerModule(new ConstraintViolationProblemModule());
    }

    @Test
    void shouldFetchAllEmployeeRoleMappings() throws Exception {
        given(employeeRoleMappingService.findAllEmployeeRoleMappings()).willReturn(this.employeeRoleMappingList);

        this.mockMvc
                .perform(get("/api/employee-role-mapping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeRoleMappingList.size())));
    }

    @Test
    void shouldFindEmployeeRoleMappingById() throws Exception {
        Long employeeRoleMappingId = 1L;
        EmployeeRoleMapping employeeRoleMapping = new EmployeeRoleMapping(employeeRoleMappingId, "text 1");
        given(employeeRoleMappingService.findEmployeeRoleMappingById(employeeRoleMappingId)).willReturn(Optional.of(employeeRoleMapping));

        this.mockMvc
                .perform(get("/api/employee-role-mapping/{id}", employeeRoleMappingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(employeeRoleMapping.getText())));
    }

    @Test
    void shouldReturn404WhenFetchingNonExistingEmployeeRoleMapping() throws Exception {
        Long employeeRoleMappingId = 1L;
        given(employeeRoleMappingService.findEmployeeRoleMappingById(employeeRoleMappingId)).willReturn(Optional.empty());

        this.mockMvc
                .perform(get("/api/employee-role-mapping/{id}", employeeRoleMappingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewEmployeeRoleMapping() throws Exception {
        given(employeeRoleMappingService.saveEmployeeRoleMapping(any(EmployeeRoleMapping.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        EmployeeRoleMapping employeeRoleMapping = new EmployeeRoleMapping(1L, "some text");
        this.mockMvc
                .perform(
                        post("/api/employee-role-mapping")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employeeRoleMapping)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", is(employeeRoleMapping.getText())));
    }

    @Test
    void shouldReturn400WhenCreateNewEmployeeRoleMappingWithoutText() throws Exception {
        EmployeeRoleMapping employeeRoleMapping = new EmployeeRoleMapping(null, null);

        this.mockMvc
                .perform(
                        post("/api/employee-role-mapping")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employeeRoleMapping)))
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
    void shouldUpdateEmployeeRoleMapping() throws Exception {
        Long employeeRoleMappingId = 1L;
        EmployeeRoleMapping employeeRoleMapping = new EmployeeRoleMapping(employeeRoleMappingId, "Updated text");
        given(employeeRoleMappingService.findEmployeeRoleMappingById(employeeRoleMappingId)).willReturn(Optional.of(employeeRoleMapping));
        given(employeeRoleMappingService.saveEmployeeRoleMapping(any(EmployeeRoleMapping.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        this.mockMvc
                .perform(
                        put("/api/employee-role-mapping/{id}", employeeRoleMapping.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employeeRoleMapping)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(employeeRoleMapping.getText())));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingEmployeeRoleMapping() throws Exception {
        Long employeeRoleMappingId = 1L;
        given(employeeRoleMappingService.findEmployeeRoleMappingById(employeeRoleMappingId)).willReturn(Optional.empty());
        EmployeeRoleMapping employeeRoleMapping = new EmployeeRoleMapping(employeeRoleMappingId, "Updated text");

        this.mockMvc
                .perform(
                        put("/api/employee-role-mapping/{id}", employeeRoleMappingId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employeeRoleMapping)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteEmployeeRoleMapping() throws Exception {
        Long employeeRoleMappingId = 1L;
        EmployeeRoleMapping employeeRoleMapping = new EmployeeRoleMapping(employeeRoleMappingId, "Some text");
        given(employeeRoleMappingService.findEmployeeRoleMappingById(employeeRoleMappingId)).willReturn(Optional.of(employeeRoleMapping));
        doNothing().when(employeeRoleMappingService).deleteEmployeeRoleMappingById(employeeRoleMapping.getId());

        this.mockMvc
                .perform(delete("/api/employee-role-mapping/{id}", employeeRoleMapping.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(employeeRoleMapping.getText())));
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingEmployeeRoleMapping() throws Exception {
        Long employeeRoleMappingId = 1L;
        given(employeeRoleMappingService.findEmployeeRoleMappingById(employeeRoleMappingId)).willReturn(Optional.empty());

        this.mockMvc
                .perform(delete("/api/employee-role-mapping/{id}", employeeRoleMappingId))
                .andExpect(status().isNotFound());
    }
}
