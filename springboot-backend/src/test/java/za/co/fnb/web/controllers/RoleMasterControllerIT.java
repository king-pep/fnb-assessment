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
import za.co.fnb.domain.RoleMaster;
import za.co.fnb.repositories.RoleMasterRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class RoleMasterControllerIT extends AbstractIntegrationTest {

    @Autowired private RoleMasterRepository roleMasterRepository;

    private List<RoleMaster> roleMasterList = null;

    @BeforeEach
    void setUp() {
        roleMasterRepository.deleteAll();

        roleMasterList = new ArrayList<>();
        roleMasterList.add(new RoleMaster(1L, "First RoleMaster","Active"));
        roleMasterList.add(new RoleMaster(2L, "Second RoleMaster","Active"));
        roleMasterList.add(new RoleMaster(3L, "Third RoleMaster","Active"));
        roleMasterList = roleMasterRepository.saveAll(roleMasterList);
    }

    @Test
    void shouldFetchAllRoleMasters() throws Exception {
        this.mockMvc
                .perform(get("/api/role-master"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(roleMasterList.size())));
    }

    @Test
    void shouldFindRoleMasterById() throws Exception {
        RoleMaster roleMaster = roleMasterList.get(0);
        Long roleMasterId = roleMaster.getId();

        this.mockMvc
                .perform(get("/api/role-master/{id}", roleMasterId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName", is(roleMaster.getRoleName())));
    }

    @Test
    void shouldCreateNewRoleMaster() throws Exception {
        RoleMaster roleMaster = new RoleMaster(null, "New RoleMaster","Active");
        this.mockMvc
                .perform(
                        post("/api/role-master")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roleMaster)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenCreateNewRoleMasterWithoutText() throws Exception {
        RoleMaster roleMaster = new RoleMaster(null, null,null);

        this.mockMvc
                .perform(
                        post("/api/role-master")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roleMaster)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/problem+json")))
                .andExpect(
                        jsonPath(
                                "$.type",
                                is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.roleName", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", is("text")))
                .andExpect(jsonPath("$.violations[0].message", is("Text cannot be empty")))
                .andReturn();
    }

    @Test
    void shouldUpdateRoleMaster() throws Exception {
        RoleMaster roleMaster = roleMasterList.get(0);
        roleMaster.setRoleName("Updated RoleMaster");

        this.mockMvc
                .perform(
                        put("/api/role-master/{id}", roleMaster.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roleMaster)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName", is(roleMaster.getRoleName())));
    }

    @Test
    void shouldDeleteRoleMaster() throws Exception {
        RoleMaster roleMaster = roleMasterList.get(0);

        this.mockMvc
                .perform(delete("/api/role-master/{id}", roleMaster.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName", is(roleMaster.getRoleName())));
    }
}
