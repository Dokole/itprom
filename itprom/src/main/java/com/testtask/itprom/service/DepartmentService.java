package com.testtask.itprom.service;

import com.testtask.itprom.domain.Department;

import java.util.List;

public interface DepartmentService {

    Department getDepartmentById(Long id);

    List<Department> getAllDepartments();

    Department createDepartment(Department department);

    Department updateDepartment(Department department);

    void deleteDepartmentById(Long id);

    void deleteAllDepartments();
}
