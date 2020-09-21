package com.testtask.itprom.controller;

import com.testtask.itprom.domain.Department;
import com.testtask.itprom.exceptions.BadRequestException;
import com.testtask.itprom.model.DepartmentModel;
import com.testtask.itprom.service.DepartmentService;
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
@RequestMapping(value = "/departments")
public class DepartmentControllerImp implements DepartmentController {

    private final DepartmentService departmentService;
    private final CastDomainToModel castDomainToModel;
    @Autowired
    public DepartmentControllerImp(DepartmentService departmentService, CastDomainToModel castDomainToModel) {
        this.departmentService = departmentService;
        this.castDomainToModel = castDomainToModel;
    }

    @Override
    @GetMapping("/{id}")
    public String getDepartmentById(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        DepartmentModel departmentModel = castDomainToModel.departmentToModel(department);
        model.addAttribute("department", departmentModel);
        return "/database/departments/department";
    }

    @Override
    @GetMapping("/getAll")
    public String getAllDepartments(Model model) {
        List<Department> departments = departmentService.getAllDepartments();
        List<DepartmentModel> departmentModels =
                departments.stream().map(castDomainToModel::departmentToModel).collect(Collectors.toList());
        model.addAttribute("departmentsList", departmentModels);
        return "/database/departments/departmentsTable";
    }

    @Override
    @GetMapping(value = "/create")
    public String createDepartmentForm(Model model) {
        if (!model.containsAttribute("newDepartment")) {
            model.addAttribute("newDepartment", new DepartmentModel());
        }
        if (!model.containsAttribute("departmentsList")) {
            model.addAttribute("departmentsList", initializeDepartmentModels());
        }
        return "/database/departments/departmentCreate";
    }

    @Override
    @PostMapping(value = "/create")
    public String createDepartment(@Valid @ModelAttribute("newDepartment") DepartmentModel departmentModel,
                                   Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("departmentsList", initializeDepartmentModels());
            return "/database/departments/departmentCreate";
        }
        Department department = castDomainToModel.departmentModelToDomain(departmentModel);
        if (departmentModel.getParentDepartmentId() != null) {
            department.setParentDepartment(departmentService.getDepartmentById(departmentModel.getParentDepartmentId()));
        }
        department = departmentService.createDepartment(department);
        departmentModel = castDomainToModel.departmentToModel(department);

        model.addAttribute("department", departmentModel);
        return "redirect:/departments/" + departmentModel.getId();
    }

    @Override
    @GetMapping("/{id}/update")
    public String updateDepartmentForm(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("department")) {
            DepartmentModel departmentModel = castDomainToModel.departmentToModel(departmentService.getDepartmentById(id));
            model.addAttribute("department", departmentModel);
        }
        if (!model.containsAttribute("departmentsList")) {
            model.addAttribute("departmentsList", initializeDepartmentModels());
        }
        return "/database/departments/departmentUpdate";
    }

    @Override
    @PostMapping("/update")
    public String updateDepartment(@Valid @ModelAttribute("department") DepartmentModel departmentModel, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("departmentsList", initializeDepartmentModels());
            return "/database/departments/departmentUpdate";
        }
        Department department = castDomainToModel.departmentModelToDomain(departmentModel);
        if (departmentModel.getParentDepartmentId() != null) {
            if (departmentModel.getParentDepartmentId().equals(department.getId())) {
                throw new BadRequestException("Department cannot be the head of itself");
            }
            department.setParentDepartment(departmentService.getDepartmentById(departmentModel.getParentDepartmentId()));
        }
        department = departmentService.updateDepartment(department);
        DepartmentModel updatedDepartment = castDomainToModel.departmentToModel(department);

        model.addAttribute("department", updatedDepartment);
        return "redirect:/departments/" + updatedDepartment.getId();
    }

    @Override
    @GetMapping("/{id}/delete")
    public String deleteDepartmentById(@PathVariable Long id, Model model) {
        departmentService.deleteDepartmentById(id);
        return "redirect:/departments/getAll";
    }

    private List<DepartmentModel> initializeDepartmentModels() {
        List<Department> departments = departmentService.getAllDepartments();
        List<DepartmentModel> departmentModels = departments.stream()
                .map(castDomainToModel::departmentToModel).collect(Collectors.toList());
        departmentModels.add(DepartmentModel.nullValueDepartment());
        return departmentModels;
    }

}
