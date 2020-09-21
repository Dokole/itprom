package com.testtask.itprom.repository;

import com.testtask.itprom.domain.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
