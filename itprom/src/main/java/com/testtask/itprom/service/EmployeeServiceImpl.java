package com.testtask.itprom.service;

import com.google.inject.internal.util.Lists;
import com.testtask.itprom.domain.Department;
import com.testtask.itprom.domain.Employee;
import com.testtask.itprom.exceptions.BadRequestException;
import com.testtask.itprom.exceptions.NotFoundException;
import com.testtask.itprom.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "employees")
public class EmployeeServiceImpl implements EmployeeService {

    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Cacheable(key = "#root.method.name.concat(#id)")
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            throw new NotFoundException("No employee found by id=" + id);
        }
        return employee;
    }

    @Override
    @Cacheable(key = "#root.method.name")
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        List<Employee> employees = Lists.newArrayList(employeeRepository.findAll());
        if (employees == null || employees.isEmpty()) {
            return new ArrayList<>();
        }
        return employees;
    }

    @Override
    @CacheEvict(cacheNames = {"employees"})
    public Employee createEmployee(Employee employee) {
        if (employee.getId() != null) {
            throw new BadRequestException("Id=" + employee.getId() + " should be null to create an employee. Can't be saved.");
        }
        return employeeRepository.save(employee);
    }

    @Override
    @CacheEvict(cacheNames = {"employees"})
    public Employee updateEmployee(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            throw new BadRequestException("Can't update employee with id=" + employee.getId() +
                    ". It doesn't exists, need to create it first.");
        }
        return employeeRepository.save(employee);
    }

    @Override
    @CacheEvict(cacheNames = {"employees"})
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = {"employees"})
    public void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }
}
