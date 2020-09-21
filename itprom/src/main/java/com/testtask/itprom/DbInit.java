package com.testtask.itprom;

import com.testtask.itprom.domain.Department;
import com.testtask.itprom.domain.Employee;
import com.testtask.itprom.domain.Profession;
import com.testtask.itprom.service.DepartmentService;
import com.testtask.itprom.service.EmployeeService;
import com.testtask.itprom.service.ProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("dbInitializer")
public class DbInit {

    @Value("${database.initialize}")
    private boolean initialize;

    private final ProfessionService professionService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @Autowired
    public DbInit(ProfessionService professionService, EmployeeService employeeService, DepartmentService departmentService) {
        this.professionService = professionService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @PostConstruct
    private void init() {
        if(initialize) {
            Department department = new Department();
            department.setName("Test 1 department");
            department.setCommentary("Comment on test 1 department");
            department = departmentService.createDepartment(department);
            System.out.println(department.getId());

            Department department2 = new Department();
            department2.setName("Test 2 department");
            department2.setCommentary("Comment on test 2 department");
            department2.setParentDepartment(department);
            department2 = departmentService.createDepartment(department2);
            System.out.println(department2.getId());

            Profession profession1 = new Profession();
            profession1.setName("Test 1 profession");
            profession1.setCommentary("Comment on test 1 profession");
            profession1 = professionService.createProfession(profession1);
            System.out.println(profession1.getId());


            Employee employee1 = new Employee();
            employee1.setFirstName("Vlad");
            employee1.setLastName("Leni");
            employee1.setPatronymic("Illy");
            employee1.setCommentary("Comment on test 1 employee");
            employee1.setDepartment(department);
            employee1.setProfession(profession1);
            employee1 = employeeService.createEmployee(employee1);

            Employee employee2 = new Employee();
            employee2.setFirstName("Oleg");
            employee2.setLastName("Komo");
            employee2.setPatronymic("Grigo");
            employee2.setCommentary("Comment on test 2 employee");
            employee2.setDepartment(department2);
            employee2.setProfession(profession1);
            employee2 = employeeService.createEmployee(employee2);
        }
    }
}
