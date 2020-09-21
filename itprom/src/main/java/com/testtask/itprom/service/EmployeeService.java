package com.testtask.itprom.service;

import com.testtask.itprom.domain.Department;
import com.testtask.itprom.domain.Employee;

import java.util.List;

public interface EmployeeService {

    Employee getEmployeeById(Long id);

    List<Employee> getAllEmployees();

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    void deleteEmployeeById(Long id);

    void deleteAllEmployees();
}
