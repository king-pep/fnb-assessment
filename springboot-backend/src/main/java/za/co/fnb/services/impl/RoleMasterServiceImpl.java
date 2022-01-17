package za.co.fnb.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.fnb.domain.RoleMaster;
import za.co.fnb.repositories.RoleMasterRepository;
import za.co.fnb.services.RoleMasterService;

import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class RoleMasterServiceImpl implements RoleMasterService {

    private final RoleMasterRepository roleMasterRepository;

    @Autowired
    public RoleMasterServiceImpl(RoleMasterRepository roleMasterRepository) {
        this.roleMasterRepository = roleMasterRepository;
    }

    public List<RoleMaster> findAllRoleMasters() {
        return roleMasterRepository.findAll();
    }

    public Optional<RoleMaster> findRoleMasterById(Long id) {
        return roleMasterRepository.findById(id);
    }

    public RoleMaster saveRoleMaster(RoleMaster roleMaster) {
        return roleMasterRepository.save(roleMaster);
    }

    public void deleteRoleMasterById(Long id) {
        roleMasterRepository.deleteById(id);
    }
}
