package com.testtask.itprom.service;

import com.testtask.itprom.domain.Employee;
import com.testtask.itprom.exceptions.BadRequestException;
import com.testtask.itprom.exceptions.NotFoundException;
import com.testtask.itprom.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private static Employee savedEmployee;
    private static Employee unsavedEmployee;


    @BeforeAll
    private static void init() {
        Employee savedE = new Employee();
        savedE.setId(1L);
        savedE.setFirstName("Jon");
        savedE.setLastName("Doe");
        savedE.setPatronymic("Vas");
        savedE.setCommentary("Test employee saved");
        savedEmployee = savedE;

        Employee unsavedE = new Employee();
        unsavedE.setFirstName("Jon");
        unsavedE.setLastName("Doe");
        unsavedE.setPatronymic("Vas");
        unsavedE.setCommentary("Test employee saved");
        unsavedEmployee = unsavedE;
    }

    @Test
    public void getEmployeeByIdTest() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(savedEmployee));
        when(employeeRepository.findById(3L)).thenReturn(Optional.empty());

        Assert.isTrue(employeeService.getEmployeeById(1L).equals(savedEmployee), "Error while getting an employee by id");
        Assertions.assertThrows(NotFoundException.class, () -> employeeService.getEmployeeById(3L));
    }

    @Test
    public void getAllDepartmentsTest() {
        when(employeeRepository.findAll()).thenReturn(List.of(savedEmployee));
        Assert.isTrue(employeeService.getAllEmployees().size() == 1, "Error while getting all employees");
    }

    @Test
    public void getAllDepartments_ErrorTest() {
        when(employeeRepository.findAll()).thenReturn(List.of());
        Assert.isTrue(employeeService.getAllEmployees().size() == 0,
                "Error while testing getting all employees as an empty list");
    }

    @Test
    public void createEmployeeTest() {
        when(employeeRepository.save(unsavedEmployee)).thenReturn(savedEmployee);
        Employee result = employeeService.createEmployee(unsavedEmployee);
        Assert.isTrue(result.equals(savedEmployee), "Employee wasn't saved");
    }

    @Test
    public void createEmployee_WithExistedIdTest() {
        when(employeeRepository.save(savedEmployee)).thenReturn(savedEmployee);
        Assertions.assertThrows(BadRequestException.class, () -> employeeService.createEmployee(savedEmployee));
        verifyNoInteractions(employeeRepository);
    }

    @Test
    public void updateEmployeeTest() {
        when(employeeRepository.save(savedEmployee)).thenReturn(savedEmployee);
        when(employeeRepository.existsById(savedEmployee.getId())).thenReturn(true);
        Employee result = employeeService.updateEmployee(savedEmployee);
        Assert.isTrue(result.equals(savedEmployee), "Employee wasn't updated");
    }

    @Test
    public void updateEmployee_WithoutExistedIdTest() {
        when(employeeRepository.save(savedEmployee)).thenReturn(savedEmployee);
        when(employeeRepository.existsById(savedEmployee.getId())).thenReturn(false);
        Assertions.assertThrows(BadRequestException.class, () -> employeeService.updateEmployee(savedEmployee));
        verify(employeeRepository, never()).save(savedEmployee);
    }


}
