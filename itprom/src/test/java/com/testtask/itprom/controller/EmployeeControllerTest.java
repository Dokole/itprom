package com.testtask.itprom.controller;

import com.testtask.itprom.domain.Department;
import com.testtask.itprom.domain.Employee;
import com.testtask.itprom.domain.Profession;
import com.testtask.itprom.exceptions.NotFoundException;
import com.testtask.itprom.model.DepartmentModel;
import com.testtask.itprom.model.EmployeeModel;
import com.testtask.itprom.model.ProfessionModel;
import com.testtask.itprom.service.DepartmentService;
import com.testtask.itprom.service.EmployeeService;
import com.testtask.itprom.service.ProfessionService;
import com.testtask.itprom.util.CastDomainToModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers = EmployeeControllerImp.class)
@ExtendWith(SpringExtension.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private DepartmentService departmentService;
    @MockBean
    private ProfessionService professionService;
    @MockBean
    private CastDomainToModel castDomainToModel;

    @InjectMocks
    private EmployeeControllerImp employeeController;

    private static Employee unsavedEmployee;
    private static EmployeeModel unsavedEmployeeModel;

    private static Employee savedEmployee;
    private static EmployeeModel savedEmployeeModel;

    private static Department savedDepartment;
    private static DepartmentModel savedDepartmentModel;

    private static Profession savedProfession;
    private static ProfessionModel savedProfessionModel;

    @BeforeAll
    public static void init() {
        Department department3 = new Department();
        department3.setName("Test 1 saved department");
        department3.setCommentary("Comment on test 1 saved department");
        department3.setId(1L);
        savedDepartment = department3;

        DepartmentModel departmentModel3 = new DepartmentModel();
        departmentModel3.setName("Test 1 saved department");
        departmentModel3.setCommentary("Comment on test 1 saved department");
        departmentModel3.setId(1L);
        savedDepartmentModel = departmentModel3;

        Profession savedP = new Profession();
        savedP.setName("Test saved profession");
        savedP.setCommentary("Comment on test saved profession");
        savedP.setId(1L);
        savedProfession = savedP;

        ProfessionModel savedPM = new ProfessionModel();
        savedPM.setName("Test saved profession model");
        savedPM.setCommentary("Comment on Test saved profession");
        savedPM.setId(1L);
        savedProfessionModel = savedPM;

        Employee unsavedE = new Employee();
        unsavedE.setFirstName("Jon");
        unsavedE.setLastName("Doe");
        unsavedE.setPatronymic("Vas");
        unsavedE.setCommentary("Test employee unsaved");
        unsavedE.setDepartment(savedDepartment);
        unsavedE.setProfession(savedProfession);
        unsavedEmployee = unsavedE;

        EmployeeModel unsavedEM = new EmployeeModel();
        unsavedEM.setFirstName("Jon");
        unsavedEM.setLastName("Doe");
        unsavedEM.setPatronymic("Vas");
        unsavedEM.setCommentary("Test employee unsaved");
        unsavedEM.setDepartment(savedDepartmentModel);
        unsavedEM.setProfession(savedProfessionModel);
        unsavedEmployeeModel = unsavedEM;

        Employee savedE = new Employee();
        savedE.setId(1L);
        savedE.setFirstName("Jon");
        savedE.setLastName("Doe");
        savedE.setPatronymic("Vas");
        savedE.setCommentary("Test employee saved");
        savedE.setDepartment(savedDepartment);
        savedE.setProfession(savedProfession);
        savedEmployee = savedE;

        EmployeeModel savedEM = new EmployeeModel();
        savedEM.setId(1L);
        savedEM.setFirstName("Jon");
        savedEM.setLastName("Doe");
        savedEM.setPatronymic("Vas");
        savedEM.setCommentary("Test employee saved");
        savedEM.setDepartment(savedDepartmentModel);
        savedEM.setProfession(savedProfessionModel);
        savedEmployeeModel = savedEM;
    }

    @Test
    public void getEmployeeByIdTest() throws Exception {

        when(employeeService.getEmployeeById(1L)).thenReturn(savedEmployee);
        when(employeeService.getEmployeeById(3L)).thenThrow(NotFoundException.class);
        when(castDomainToModel.employeeToModel(savedEmployee)).thenReturn(savedEmployeeModel);

        mockMvc.perform(get("/employees/3"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("customErrorMessage"))
                .andExpect(model().attributeExists("errorMessage"));
        verify(employeeService, times(1)).getEmployeeById(3L);
        verifyNoInteractions(castDomainToModel);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/employees/employee"))
                .andExpect(model().attribute("employee", savedEmployeeModel));

        verify(employeeService, times(1)).getEmployeeById(1L);
        verify(castDomainToModel, times(1)).employeeToModel(savedEmployee);

    }

    @Test
    public void getAllEmployeesTest() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(List.of(savedEmployee));
        when(castDomainToModel.employeeToModel(savedEmployee)).thenReturn(savedEmployeeModel);

        mockMvc.perform(get("/employees/getAll"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/employees/employeesTable"))
                .andExpect(model().attribute("employeesList", hasSize(1)))
                .andExpect(model().attribute("employeesList", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("firstName", is("Jon")),
                                hasProperty("commentary", is("Test employee saved")),
                                hasProperty("department", is(savedDepartmentModel)),
                                hasProperty("profession", is(savedProfessionModel))
                        )
                )));
        verify(employeeService, times(1)).getAllEmployees();
        verify(castDomainToModel, times(1)).employeeToModel(savedEmployee);
    }

    @Test
    public void createEmployeeFormTest() throws Exception {

        mockMvc.perform(get("/employees/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/employees/employeeCreate"))
                .andExpect(model().attributeExists("newEmployee"))
                .andExpect(model().attributeExists("departmentsList"))
                .andExpect(model().attributeExists("professionsList"));
    }

    @Test
    public void createEmployeeTest() throws Exception {
        when(castDomainToModel.employeeModelToDomain(any())).thenReturn(unsavedEmployee);
        when(employeeService.createEmployee(unsavedEmployee)).thenReturn(savedEmployee);
        when(castDomainToModel.employeeToModel(savedEmployee)).thenReturn(savedEmployeeModel);
        when(departmentService.getDepartmentById(1L)).thenReturn(savedDepartment);
        when(professionService.getProfessionById(1L)).thenReturn(savedProfession);

        mockMvc.perform(post("/employees/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Mann")
                .param("lastName", "Douu")
                .param("patronymic", "patrony")
                .param("commentary", "")
                .param("departmentId", "1")
                .param("professionId", "1")
                .sessionAttr("newEmployee", new EmployeeModel()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employees/1"))
                .andExpect(model().attribute("employee",
                        hasProperty("firstName", is("Jon"))));

        mockMvc.perform(post("/employees/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Te")
                .param("secondName", "Dou")
                .param("commentary", "")
                .sessionAttr("newEmployee", new EmployeeModel()))
                .andExpect(model().attributeHasFieldErrors("newEmployee", "firstName"));
    }

    @Test
    public void updateEmployeeFormTest() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(savedEmployee);
        when(castDomainToModel.employeeToModel(savedEmployee)).thenReturn(savedEmployeeModel);

        mockMvc.perform(get("/employees/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/employees/employeeUpdate"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("departmentsList"))
                .andExpect(model().attributeExists("professionsList"));

    }

    @Test
    public void updateEmployeeTest() throws Exception {
        Employee updatedE = new Employee();
        updatedE.setFirstName("Updated name");
        updatedE.setLastName("Updated last name");
        updatedE.setPatronymic("partoU");
        updatedE.setCommentary("Comment on test updated Employee");
        updatedE.setId(1L);

        EmployeeModel updatedEM = new EmployeeModel();
        updatedEM.setFirstName("Updated name");
        updatedEM.setLastName("Updated last name");
        updatedEM.setPatronymic("partoU");
        updatedEM.setCommentary("Comment on test updated Employee");
        updatedEM.setId(1L);

        when(castDomainToModel.employeeModelToDomain(any())).thenReturn(updatedE);
        when(employeeService.updateEmployee(updatedE)).thenReturn(updatedE);
        when(castDomainToModel.employeeToModel(updatedE)).thenReturn(updatedEM);

        mockMvc.perform(post("/employees/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Updated name")
                .param("lastName", "Updated last name")
                .param("patronymic", "partoU")
                .param("commentary", "Comment on test updated Employee")
                .sessionAttr("department", new DepartmentModel()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employees/1"))
                .andExpect(model().attribute("employee",
                        hasProperty("firstName", is("Updated name"))));

        mockMvc.perform(post("/employees/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Te")
                .param("secondName", "Updated last name")
                .param("commentary", "")
                .sessionAttr("employee", new DepartmentModel()))
                .andExpect(model().attributeHasFieldErrors("employee", "firstName"));
    }


    @Test
    public void deleteEmployeeByIdTest() throws Exception {
        mockMvc.perform(get("/employees/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/employees/getAll"));
    }
}
