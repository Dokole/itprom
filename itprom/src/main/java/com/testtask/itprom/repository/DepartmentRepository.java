package com.testtask.itprom.repository;

import com.testtask.itprom.domain.Department;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
}
