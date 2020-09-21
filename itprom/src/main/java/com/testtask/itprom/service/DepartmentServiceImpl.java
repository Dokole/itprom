package com.testtask.itprom.service;

import com.google.inject.internal.util.Lists;
import com.testtask.itprom.domain.Department;
import com.testtask.itprom.exceptions.BadRequestException;
import com.testtask.itprom.exceptions.NotFoundException;
import com.testtask.itprom.repository.DepartmentRepository;
import org.hibernate.annotations.Cache;
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
@CacheConfig(cacheNames = "departments")
public class DepartmentServiceImpl implements DepartmentService {

    Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Cacheable(key = "#root.method.name.concat(#id)")
    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if (department == null) {
            throw new NotFoundException("No department found by id=" + id);
        }
        return department;
    }

    @Override
    @Cacheable(key = "#root.method.name")
    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        List<Department> departments = Lists.newArrayList(departmentRepository.findAll());
        if (departments == null || departments.isEmpty()) {
            return new ArrayList<>();
        }
        return departments;
    }

    @Override
    @CacheEvict(cacheNames = {"departments", "employees"}, allEntries = true)
    public Department createDepartment(Department department) {
        if (department.getId() != null) {
            throw new BadRequestException("Id=" + department.getId() + " should be null to create a department. Can't be saved.");
        }
        return departmentRepository.save(department);
    }

    @Override
    @CacheEvict(cacheNames = {"departments", "employees"}, allEntries = true)
    public Department updateDepartment(Department department) {
        if (!departmentRepository.existsById(department.getId())) {
            throw new BadRequestException("Can't update department with id=" + department.getId() +
                    ". It doesn't exists, need to create it first.");
        }
        return departmentRepository.save(department);
    }

    @Override
    @CacheEvict(cacheNames = {"departments", "employees"}, allEntries = true)
    public void deleteDepartmentById(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = {"departments", "employees"}, allEntries = true)
    public void deleteAllDepartments() {
        departmentRepository.deleteAll();
    }
}
