package com.testtask.itprom.controller;


import com.testtask.itprom.domain.Department;
import com.testtask.itprom.domain.Employee;
import com.testtask.itprom.domain.Profession;
import com.testtask.itprom.model.DepartmentModel;
import com.testtask.itprom.model.EmployeeModel;
import com.testtask.itprom.model.ProfessionModel;
import com.testtask.itprom.service.DepartmentService;
import com.testtask.itprom.service.EmployeeService;
import com.testtask.itprom.service.ProfessionService;
import com.testtask.itprom.util.CastDomainToModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeControllerImp implements EmployeeController {

    private final CastDomainToModel castDomainToModel;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final ProfessionService professionService;

    @Autowired
    public EmployeeControllerImp(CastDomainToModel castDomainToModel, EmployeeService employeeService, DepartmentService departmentService, ProfessionService professionService) {
        this.castDomainToModel = castDomainToModel;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.professionService = professionService;
    }

    @Override
    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        EmployeeModel employee = castDomainToModel.employeeToModel(employeeService.getEmployeeById(id));
        model.addAttribute("employee", employee);
        return "/database/employees/employee";
    }

    @Override
    @GetMapping("/getAll")
    public String getAllEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeModel> employeeModels =
                employees.stream().map(castDomainToModel::employeeToModel).collect(Collectors.toList());
        model.addAttribute("employeesList", employeeModels);
        return "/database/employees/employeesTable";
    }

    @Override
    @GetMapping("/create")
    public String createEmployeeForm(Model model) {
        if (!model.containsAttribute("newEmployee")) {
            model.addAttribute("newEmployee", new EmployeeModel());
        }
        if (!model.containsAttribute("departmentsList")) {
            model.addAttribute("departmentsList", initializeDepartmentModels());
        }
        if (!model.containsAttribute("professionsList")) {
            model.addAttribute("professionsList", initializeProfessionModels());
        }
        return "/database/employees/employeeCreate";
    }

    @Override
    @PostMapping("/create")
    public String createEmployee(@Valid @ModelAttribute("newEmployee") EmployeeModel employeeModel, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("departmentsList", initializeDepartmentModels());
            model.addAttribute("professionsList", initializeProfessionModels());
            return "/database/employees/employeeCreate";
        }
        Employee employee = castDomainToModel.employeeModelToDomain(employeeModel);
        if (employeeModel.getDepartmentId() != null) {
            employee.setDepartment(departmentService.getDepartmentById(employeeModel.getDepartmentId()));
        }
        if (employeeModel.getProfessionId() != null) {
            employee.setProfession(professionService.getProfessionById(employeeModel.getProfessionId()));
        }

        employee = employeeService.createEmployee(employee);
        EmployeeModel createdEmployee = castDomainToModel.employeeToModel(employee);

        model.addAttribute("employee", createdEmployee);
        return "redirect:/employees/" + createdEmployee.getId();
    }

    @Override
    @GetMapping("/{id}/update")
    public String updateEmployeeForm(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("employee")) {
            EmployeeModel employeeModel = castDomainToModel.employeeToModel(employeeService.getEmployeeById(id));
            model.addAttribute("employee", employeeModel);
        }
        if (!model.containsAttribute("departmentsList")) {
            model.addAttribute("departmentsList", initializeDepartmentModels());
        }
        if (!model.containsAttribute("professionsList")) {
            model.addAttribute("professionsList", initializeProfessionModels());
        }
        return "/database/employees/employeeUpdate";
    }

    @Override
    @PostMapping("/update")
    public String updateEmployee(@Valid @ModelAttribute("employee") EmployeeModel employeeModel,
                                 Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("departmentsList", initializeDepartmentModels());
            model.addAttribute("professionsList", initializeProfessionModels());
            return "/database/employees/employeeUpdate";
        }

        Employee employee = castDomainToModel.employeeModelToDomain(employeeModel);
        if (employeeModel.getProfessionId() != null) {
            employee.setProfession(professionService.getProfessionById(employeeModel.getProfessionId()));
        }
        if (employeeModel.getDepartmentId() != null) {
            employee.setDepartment(departmentService.getDepartmentById(employeeModel.getDepartmentId()));
        }
        employee = employeeService.updateEmployee(employee);
        EmployeeModel updatedEmployee = castDomainToModel.employeeToModel(employee);

        model.addAttribute("employee", updatedEmployee);
        return "redirect:/employees/" + updatedEmployee.getId();
    }

    @Override
    @GetMapping("/{id}/delete")
    public String deleteEmployeeById(@PathVariable Long id, Model model) {
        employeeService.deleteEmployeeById(id);
        return "redirect:/employees/getAll";
    }

    private List<DepartmentModel> initializeDepartmentModels() {
        List<Department> departments = departmentService.getAllDepartments();
        List<DepartmentModel> departmentModels = departments.stream()
                .map(castDomainToModel::departmentToModel).collect(Collectors.toList());
        departmentModels.add(DepartmentModel.nullValueDepartment());
        return departmentModels;
    }

    private List<ProfessionModel> initializeProfessionModels() {
        List<Profession> professions = professionService.getAllProfessions();
        List<ProfessionModel> departmentModels = professions.stream()
                .map(castDomainToModel::professionToModel).collect(Collectors.toList());
        departmentModels.add(ProfessionModel.nullValueProfession());
        return departmentModels;
    }
}
