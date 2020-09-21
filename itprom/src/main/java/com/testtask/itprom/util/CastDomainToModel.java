package com.testtask.itprom.util;

import com.testtask.itprom.domain.Department;
import com.testtask.itprom.domain.Employee;
import com.testtask.itprom.domain.Profession;
import com.testtask.itprom.model.DepartmentModel;
import com.testtask.itprom.model.EmployeeModel;
import com.testtask.itprom.model.ProfessionModel;
import org.springframework.stereotype.Component;

@Component("castDomainToModel")
public class CastDomainToModel {

    public ProfessionModel professionToModel(Profession profession) {
        ProfessionModel professionModel = new ProfessionModel();
        professionModel.setId(profession.getId());
        professionModel.setName(profession.getName());
        professionModel.setCommentary(profession.getCommentary());
        return professionModel;
    }

    public Profession professionModelToDomain(ProfessionModel professionModel) {
        Profession profession = new Profession();
        profession.setId(professionModel.getId());
        profession.setName(professionModel.getName());
        profession.setCommentary(professionModel.getCommentary());
        return profession;
    }

    public DepartmentModel departmentToModel(Department department) {
        DepartmentModel departmentModel = new DepartmentModel();
        departmentModel.setId(department.getId());
        departmentModel.setName(department.getName());
        departmentModel.setCommentary(department.getCommentary());

        if (department.getParentDepartment() != null) {
            departmentModel.setParentDepartment(departmentToModel(department.getParentDepartment()));
        }
        return departmentModel;
    }

    public Department departmentModelToDomain(DepartmentModel departmentModel) {
        Department department = new Department();
        department.setId(departmentModel.getId());
        department.setName(departmentModel.getName());
        department.setCommentary(departmentModel.getCommentary());

        if (departmentModel.getParentDepartment() != null) {
            department.setParentDepartment(departmentModelToDomain(departmentModel.getParentDepartment()));
        }
        return department;
    }

    public EmployeeModel employeeToModel(Employee employee) {
        EmployeeModel employeeModel = new EmployeeModel();
        employeeModel.setId(employee.getId());
        employeeModel.setFirstName(employee.getFirstName());
        employeeModel.setLastName(employee.getLastName());
        employeeModel.setPatronymic(employee.getPatronymic());
        employeeModel.setCommentary(employee.getCommentary());

        if (employee.getDepartment() != null) {
            employeeModel.setDepartment(departmentToModel(employee.getDepartment()));
        }

        if (employee.getProfession() != null) {
            employeeModel.setProfession(professionToModel(employee.getProfession()));
        }

        return employeeModel;
    }

    public Employee employeeModelToDomain(EmployeeModel employeeModel) {
        Employee employee = new Employee();
        employee.setId(employeeModel.getId());
        employee.setFirstName(employeeModel.getFirstName());
        employee.setLastName(employeeModel.getLastName());
        employee.setPatronymic(employeeModel.getPatronymic());
        employee.setCommentary(employeeModel.getCommentary());

        if (employeeModel.getDepartment() != null) {
            employee.setDepartment(departmentModelToDomain(employeeModel.getDepartment()));
        }

        if (employeeModel.getProfession() != null) {
            employee.setProfession(professionModelToDomain(employeeModel.getProfession()));
        }

        return employee;
    }
}
