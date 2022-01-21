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
import za.co.fnb.domain.RoleMaster;
import za.co.fnb.services.RoleMasterService;
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

@WebMvcTest(controllers = RoleMasterController.class)
@ActiveProfiles("test")
class RoleMasterControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private RoleMasterService roleMasterService;

    @Autowired private ObjectMapper objectMapper;

    private List<RoleMaster> roleMasterList;

    @BeforeEach
    void setUp() {
        this.roleMasterList = new ArrayList<>();
        this.roleMasterList.add(new RoleMaster(1L, "text 1","Active"));
        this.roleMasterList.add(new RoleMaster(2L, "text 2","Active"));
        this.roleMasterList.add(new RoleMaster(3L, "text 3","Active"));

        objectMapper.registerModule(new ProblemModule());
        objectMapper.registerModule(new ConstraintViolationProblemModule());
    }

    @Test
    void shouldFetchAllRoleMasters() throws Exception {
        given(roleMasterService.findAllRoleMasters()).willReturn(this.roleMasterList);

        this.mockMvc
                .perform(get("/api/role-master"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(roleMasterList.size())));
    }

    @Test
    void shouldFindRoleMasterById() throws Exception {
        Long roleMasterId = 1L;
        RoleMaster roleMaster = new RoleMaster(roleMasterId, "text 1","Active");
        given(roleMasterService.findRoleMasterById(roleMasterId)).willReturn(Optional.of(roleMaster));

        this.mockMvc
                .perform(get("/api/role-master/{id}", roleMasterId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName", is(roleMaster.getRoleName())));
    }

    @Test
    void shouldReturn404WhenFetchingNonExistingRoleMaster() throws Exception {
        Long roleMasterId = 1L;
        given(roleMasterService.findRoleMasterById(roleMasterId)).willReturn(Optional.empty());

        this.mockMvc
                .perform(get("/api/role-master/{id}", roleMasterId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewRoleMaster() throws Exception {
        given(roleMasterService.saveRoleMaster(any(RoleMaster.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        RoleMaster roleMaster = new RoleMaster(1L, "some text","Active");
        this.mockMvc
                .perform(
                        post("/api/role-master")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roleMaster)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.roleName", is(roleMaster.getRoleName())));
    }

    @Test
    void shouldReturn400WhenCreateNewRoleMasterWithoutRoleName() throws Exception {
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
        Long roleMasterId = 1L;
        RoleMaster roleMaster = new RoleMaster(roleMasterId, "Updated text","Active");
        given(roleMasterService.findRoleMasterById(roleMasterId)).willReturn(Optional.of(roleMaster));
        given(roleMasterService.saveRoleMaster(any(RoleMaster.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        this.mockMvc
                .perform(
                        put("/api/role-master/{id}", roleMaster.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roleMaster)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName", is(roleMaster.getRoleName())));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingRoleMaster() throws Exception {
        Long roleMasterId = 1L;
        given(roleMasterService.findRoleMasterById(roleMasterId)).willReturn(Optional.empty());
        RoleMaster roleMaster = new RoleMaster(roleMasterId, "Updated text","Active");

        this.mockMvc
                .perform(
                        put("/api/role-master/{id}", roleMasterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roleMaster)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteRoleMaster() throws Exception {
        Long roleMasterId = 1L;
        RoleMaster roleMaster = new RoleMaster(roleMasterId, "Some text","Active");
        given(roleMasterService.findRoleMasterById(roleMasterId)).willReturn(Optional.of(roleMaster));
        doNothing().when(roleMasterService).deleteRoleMasterById(roleMaster.getId());

        this.mockMvc
                .perform(delete("/api/role-master/{id}", roleMaster.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName", is(roleMaster.getRoleName())));
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingRoleMaster() throws Exception {
        Long roleMasterId = 1L;
        given(roleMasterService.findRoleMasterById(roleMasterId)).willReturn(Optional.empty());

        this.mockMvc
                .perform(delete("/api/role-master/{id}", roleMasterId))
                .andExpect(status().isNotFound());
    }
}
