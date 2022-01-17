package za.co.fnb.web.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import za.co.fnb.common.AbstractIntegrationTest;
import za.co.fnb.domain.EmployeeRoleMapping;
import za.co.fnb.repositories.EmployeeRoleMappingRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class EmployeeRoleMappingControllerIT extends AbstractIntegrationTest {

    @Autowired private EmployeeRoleMappingRepository employeeRoleMappingRepository;

    private List<EmployeeRoleMapping> employeeRoleMappingList = null;

    @BeforeEach
    void setUp() {
        employeeRoleMappingRepository.deleteAll();

        employeeRoleMappingList = new ArrayList<>();
        employeeRoleMappingList.add(new EmployeeRoleMapping(1L, "First EmployeeRoleMapping"));
        employeeRoleMappingList.add(new EmployeeRoleMapping(2L, "Second EmployeeRoleMapping"));
        employeeRoleMappingList.add(new EmployeeRoleMapping(3L, "Third EmployeeRoleMapping"));
        employeeRoleMappingList = employeeRoleMappingRepository.saveAll(employeeRoleMappingList);
    }

    @Test
    void shouldFetchAllEmployeeRoleMappings() throws Exception {
        this.mockMvc
                .perform(get("/api/employee-role-mapping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeRoleMappingList.size())));
    }

    @Test
    void shouldFindEmployeeRoleMappingById() throws Exception {
        EmployeeRoleMapping employeeRoleMapping = employeeRoleMappingList.get(0);
        Long employeeRoleMappingId = employeeRoleMapping.getId();

        this.mockMvc
                .perform(get("/api/employee-role-mapping/{id}", employeeRoleMappingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(employeeRoleMapping.getText())));
    }

    @Test
    void shouldCreateNewEmployeeRoleMapping() throws Exception {
        EmployeeRoleMapping employeeRoleMapping = new EmployeeRoleMapping(null, "New EmployeeRoleMapping");
        this.mockMvc
                .perform(
                        post("/api/employee-role-mapping")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employeeRoleMapping)))
                .andExpect(status().isCreated())
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
        EmployeeRoleMapping employeeRoleMapping = employeeRoleMappingList.get(0);
        employeeRoleMapping.setText("Updated EmployeeRoleMapping");

        this.mockMvc
                .perform(
                        put("/api/employee-role-mapping/{id}", employeeRoleMapping.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employeeRoleMapping)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(employeeRoleMapping.getText())));
    }

    @Test
    void shouldDeleteEmployeeRoleMapping() throws Exception {
        EmployeeRoleMapping employeeRoleMapping = employeeRoleMappingList.get(0);

        this.mockMvc
                .perform(delete("/api/employee-role-mapping/{id}", employeeRoleMapping.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(employeeRoleMapping.getText())));
    }
}
