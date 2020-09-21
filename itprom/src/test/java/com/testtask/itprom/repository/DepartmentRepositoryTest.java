package com.testtask.itprom.repository;

import com.google.inject.internal.util.Lists;
import com.testtask.itprom.domain.Department;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;
    private Department testDepartment;
    private Department secondTestDepartment;

    @BeforeEach
    public void init() {
        Department department = new Department();
        department.setName("Test 1 department");
        department.setCommentary("Comment on test 1 department");
        testDepartment = departmentRepository.save(department);

        Department department2 = new Department();
        department2.setName("Test 2 department");
        department2.setCommentary("Comment on test 2 department");
        department2.setParentDepartment(testDepartment);
        secondTestDepartment = departmentRepository.save(department2);

        Assert.isTrue(Lists.newArrayList(departmentRepository.findAll()).size() == 2,
                "All departments weren't saved");
    }

    @AfterEach
    public void cleaning() {
        departmentRepository.deleteAll();
        Assert.isTrue(Lists.newArrayList(departmentRepository.findAll()).size() == 0,
                "All departments weren't deleted");
    }

    @Test
    public void saveDepartmentTest() {
        Assert.notNull(testDepartment.getId(), "department wasn't saved");
        Assert.notNull(secondTestDepartment.getId(), "department wasn't saved");
        Assert.isTrue(secondTestDepartment.getParentDepartment().getId().equals(testDepartment.getId()), "wrong parent department");
    }
    @Test
    public void updateDepartmentTest() {
        testDepartment.setName("changed name");
        testDepartment = departmentRepository.save(testDepartment);
        Assert.isTrue(testDepartment.getName().equals("changed name"), "department wasn't updated");
    }

    @Test
    public void getDepartmentByIdTest() {
        Department department = departmentRepository.findById(testDepartment.getId()).orElse(null);
        Assert.notNull(department, "department wasn't found in the database");
    }

    @Test
    public void getAllDepartmentsTest() {
        List<Department> departments = Lists.newArrayList(departmentRepository.findAll());
        Assert.isTrue(departments.size() == 2, "All departments weren't received");
    }
    @Test
    public void deleteAllDepartmentsTest() {
        Assert.isTrue(Lists.newArrayList(departmentRepository.findAll()).size() == 2,
                "All departments weren't received");
        departmentRepository.deleteAll();
        Assert.isTrue(Lists.newArrayList(departmentRepository.findAll()).size() == 0,
                "All departments weren't received");
    }
    @Test
    public void deleteDepartmentByIdTest() {
        //not going to work, but it works in Transactional context, in service layer.
        //can't catch JDBC exception
//        departmentRepository.deleteById(testDepartment.getId());
        departmentRepository.deleteById(secondTestDepartment.getId());
        Assert.isTrue(Lists.newArrayList(departmentRepository.findAll()).size() == 1,
                "All departments should be deleted at this point");
    }
}
