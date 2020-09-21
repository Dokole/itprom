package com.testtask.itprom.controller;

import com.testtask.itprom.model.DepartmentModel;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

public interface DepartmentController {

    String getDepartmentById(Long id, Model model);

    String getAllDepartments(Model model);

    String createDepartmentForm(Model model);

    String createDepartment(DepartmentModel departmentModel, Errors errors, Model model);

    String updateDepartmentForm(Long id, Model model);

    String updateDepartment(DepartmentModel departmentModel, Errors errors, Model model);

    String deleteDepartmentById(Long id, Model model);

}
