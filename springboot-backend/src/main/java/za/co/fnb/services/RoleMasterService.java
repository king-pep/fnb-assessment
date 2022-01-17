package za.co.fnb.services;

import za.co.fnb.domain.RoleMaster;
import za.co.fnb.repositories.RoleMasterRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface RoleMasterService {



    public List<RoleMaster> findAllRoleMasters();

    public Optional<RoleMaster> findRoleMasterById(Long id);

    public RoleMaster saveRoleMaster(RoleMaster roleMaster);

    public void deleteRoleMasterById(Long id);
}
