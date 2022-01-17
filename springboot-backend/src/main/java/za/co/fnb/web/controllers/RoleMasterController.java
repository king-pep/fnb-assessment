package za.co.fnb.web.controllers;

import za.co.fnb.domain.RoleMaster;
import za.co.fnb.services.RoleMasterService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role-master")
@Slf4j
public class RoleMasterController {

    private final RoleMasterService roleMasterService;

    @Autowired
    public RoleMasterController(RoleMasterService roleMasterService) {
        this.roleMasterService = roleMasterService;
    }

    @GetMapping
    public List<RoleMaster> getAllRoleMasters() {
        return roleMasterService.findAllRoleMasters();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleMaster> getRoleMasterById(@PathVariable Long id) {
        return roleMasterService
                .findRoleMasterById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleMaster createRoleMaster(@RequestBody @Validated RoleMaster roleMaster) {
        return roleMasterService.saveRoleMaster(roleMaster);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleMaster> updateRoleMaster(
            @PathVariable Long id, @RequestBody RoleMaster roleMaster) {
        return roleMasterService
                .findRoleMasterById(id)
                .map(
                        roleMasterObj -> {
                            roleMaster.setId(id);
                            return ResponseEntity.ok(roleMasterService.saveRoleMaster(roleMaster));
                        })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoleMaster> deleteRoleMaster(@PathVariable Long id) {
        return roleMasterService
                .findRoleMasterById(id)
                .map(
                        roleMaster -> {
                            roleMasterService.deleteRoleMasterById(id);
                            return ResponseEntity.ok(roleMaster);
                        })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
