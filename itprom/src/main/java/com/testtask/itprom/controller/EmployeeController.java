package com.testtask.itprom.controller;

import com.testtask.itprom.model.EmployeeModel;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

public interface EmployeeController {

    String getEmployeeById(Long id, Model model);

    String getAllEmployees(Model model);

    String createEmployeeForm(Model model);

    String createEmployee(EmployeeModel employeeModel, Errors errors, Model model);

    String updateEmployeeForm(Long id, Model model);

    String updateEmployee(EmployeeModel employeeModel, Errors errors, Model model);

    String deleteEmployeeById(Long id, Model model);

}
