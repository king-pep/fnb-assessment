package za.co.fnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import za.co.fnb.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByFirstName(String firstName, Pageable pageable);

    Page<Employee> findByMiddleName(String middleName, Pageable pageable);

    List<Employee> findByLastName(String lastName, Sort sort);
}
