package com.testtask.itprom.repository;

import com.google.inject.internal.util.Lists;
import com.testtask.itprom.domain.Department;
import com.testtask.itprom.domain.Employee;
import com.testtask.itprom.domain.Profession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ProfessionRepository professionRepository;

    private Employee firstTestEmployee;
    private Employee secondTestEmployee;

    @BeforeEach
    public void init() {
        Department department1 = new Department();
        department1.setName("Test 1 department");
        department1.setCommentary("Comment on test 1 department");
        department1 = departmentRepository.save(department1);

        Department department2 = new Department();
        department2.setName("Test 2 department");
        department2.setCommentary("Comment on test 2 department");
        department2.setParentDepartment(department1);
        department2 = departmentRepository.save(department2);

        Assert.isTrue(Lists.newArrayList(departmentRepository.findAll()).size() == 2,
                "All departments weren't saved");

        Profession profession1 = new Profession();
        profession1.setName("Test 1 profession");
        profession1.setCommentary("Comment on test 1 profession");
        profession1 = professionRepository.save(profession1);

        Assert.isTrue(Lists.newArrayList(professionRepository.findAll()).size() == 1,
                "All professions weren't saved");

        Employee employee1 = new Employee();
        employee1.setFirstName("Vlad");
        employee1.setLastName("Leni");
        employee1.setPatronymic("Illy");
        employee1.setCommentary("Test 1 employee");
        employee1.setDepartment(department1);
        employee1.setProfession(profession1);
        firstTestEmployee = employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("Oleg");
        employee2.setLastName("Komo");
        employee2.setPatronymic("Grigo");
        employee2.setCommentary("Test 2 employee");
        employee2.setDepartment(department2);
        employee2.setProfession(profession1);
        secondTestEmployee = employeeRepository.save(employee2);

        Assert.isTrue(Lists.newArrayList(employeeRepository.findAll()).size() == 2,
                "All employees weren't saved");

    }

    @AfterEach
    public void cleaning() {
        employeeRepository.deleteAll();
        professionRepository.deleteAll();
        departmentRepository.deleteAll();
        Assert.isTrue(Lists.newArrayList(departmentRepository.findAll()).size() == 0,
                "All departments weren't deleted");
    }

    @Test
    public void saveEmployeeTest() {
        Assert.notNull(firstTestEmployee.getId(), "employee wasn't saved");
        Assert.notNull(secondTestEmployee.getId(), "employee wasn't saved");
    }
    @Test
    public void updateEmployeeTest() {
        firstTestEmployee.setFirstName("changed name");
        firstTestEmployee = employeeRepository.save(firstTestEmployee);
        Assert.isTrue(firstTestEmployee.getFirstName().equals("changed name"), "employee wasn't updated");
    }

    @Test
    public void getEmployeeByIdTest() {
        Employee employee = employeeRepository.findById(firstTestEmployee.getId()).orElse(null);
        Assert.notNull(employee, "employee wasn't found in the database");
    }

    @Test
    public void getAllEmployeesTest() {
        List<Employee> employees = Lists.newArrayList(employeeRepository.findAll());
        Assert.isTrue(employees.size() == 2, "All employees weren't received");
    }
    @Test
    public void deleteAllEmployeesTest() {
        Assert.isTrue(Lists.newArrayList(employeeRepository.findAll()).size() == 2,
                "All employees weren't received");
        employeeRepository.deleteAll();
        Assert.isTrue(Lists.newArrayList(employeeRepository.findAll()).size() == 0,
                "All employees weren't received");
    }
    @Test
    public void deleteDEmployeeByIdTest() {
        Long id = firstTestEmployee.getId();
        employeeRepository.deleteById(id);
        Assert.isNull(employeeRepository.findById(id).orElse(null), "employees wasn't deleted");
        Assert.isTrue(Lists.newArrayList(employeeRepository.findAll()).size() == 1,
                "Number of employees wasn't changed by only 1 object");
        id = secondTestEmployee.getId();
        employeeRepository.deleteById(id);
        Assert.isTrue(Lists.newArrayList(employeeRepository.findAll()).size() == 0,
                "All employees should be deleted at this point");
    }
}
