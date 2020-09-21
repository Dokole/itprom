package com.testtask.itprom.service;

import com.testtask.itprom.domain.Department;
import com.testtask.itprom.exceptions.BadRequestException;
import com.testtask.itprom.exceptions.NotFoundException;
import com.testtask.itprom.repository.DepartmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private static Department savedDepartment;
    private static Department unsavedDepartment;


    @BeforeAll
    private static void init() {
        Department department = new Department();
        department.setName("Test 1 saved department");
        department.setCommentary("Comment on test 1 saved department");
        department.setId(1L);
        savedDepartment = department;

        Department department2 = new Department();
        department2.setName("Test 1 saved department");
        department2.setCommentary("Comment on test 1 saved department");
        unsavedDepartment = department2;

    }

    @Test
    public void getDepartmentByIdTest() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(savedDepartment));
        when(departmentRepository.findById(3L)).thenReturn(Optional.empty());

        Assert.isTrue(departmentService.getDepartmentById(1L).equals(savedDepartment), "Error while getting department by id");
        Assertions.assertThrows(NotFoundException.class, () -> departmentService.getDepartmentById(3L));
    }

    @Test
    public void getAllDepartmentsTest() {
        when(departmentRepository.findAll()).thenReturn(List.of(savedDepartment));
        Assert.isTrue(departmentService.getAllDepartments().size() == 1, "Error while getting all departments");
    }

    @Test
    public void getAllDepartments_ErrorTest() {
        when(departmentRepository.findAll()).thenReturn(List.of());
        Assert.isTrue(departmentService.getAllDepartments().size() == 0,
                "Error while testing getting all departments as an empty list");
    }

    @Test
    public void createDepartmentTest() {
        when(departmentRepository.save(unsavedDepartment)).thenReturn(savedDepartment);
        Department result = departmentService.createDepartment(unsavedDepartment);
        Assert.isTrue(result.equals(savedDepartment), "Department wasn't saved");
    }

    @Test
    public void createDepartment_WithExistedIdTest() {
        when(departmentRepository.save(savedDepartment)).thenReturn(savedDepartment);
        Assertions.assertThrows(BadRequestException.class, () -> departmentService.createDepartment(savedDepartment));
        verifyNoInteractions(departmentRepository);
    }

    @Test
    public void updateDepartmentTest() {
        when(departmentRepository.save(savedDepartment)).thenReturn(savedDepartment);
        when(departmentRepository.existsById(savedDepartment.getId())).thenReturn(true);
        Department result = departmentService.updateDepartment(savedDepartment);
        Assert.isTrue(result.equals(savedDepartment), "Department wasn't updated");
    }

    @Test
    public void updateDepartment_WithoutExistedIdTest() {
        when(departmentRepository.save(savedDepartment)).thenReturn(savedDepartment);
        when(departmentRepository.existsById(savedDepartment.getId())).thenReturn(false);
        Assertions.assertThrows(BadRequestException.class, () -> departmentService.updateDepartment(savedDepartment));
        verify(departmentRepository, never()).save(savedDepartment);
    }

}
