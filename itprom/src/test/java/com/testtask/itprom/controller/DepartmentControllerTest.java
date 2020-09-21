package com.testtask.itprom.controller;


import com.testtask.itprom.domain.Department;
import com.testtask.itprom.exceptions.NotFoundException;
import com.testtask.itprom.model.DepartmentModel;
import com.testtask.itprom.service.DepartmentService;
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

@WebMvcTest(controllers = DepartmentControllerImp.class)
@ExtendWith(SpringExtension.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;
    @MockBean
    private CastDomainToModel castDomainToModel;

    @InjectMocks
    private DepartmentControllerImp departmentController;

    private static Department unsavedDepartment1;
    private static Department savedDepartment1;
    private static DepartmentModel unsavedDepartmentModel1;
    private static DepartmentModel savedDepartmentModel1;

    private static Department unsavedDepartment2;
    private static Department savedDepartment2;
    private static DepartmentModel unsavedDepartmentModel2;
    private static DepartmentModel savedDepartmentModel2;

    @BeforeAll
    public static void init() {

        Department department1 = new Department();
        department1.setName("Test 1 unsaved department");
        department1.setCommentary("Comment on test 1 unsaved department");
        unsavedDepartment1 = department1;

        Department department3 = new Department();
        department3.setName("Test 1 saved department");
        department3.setCommentary("Comment on test 1 saved department");
        department3.setId(1L);
        savedDepartment1 = department3;

        Department department2 = new Department();
        department2.setName("Test 2 unsaved department");
        department2.setCommentary("Comment on test 2 unsaved department");
        department2.setParentDepartment(department3);
        unsavedDepartment2 = department2;

        Department department4 = new Department();
        department4.setName("Test 2 saved department");
        department4.setCommentary("Comment on test 2 saved department");
        department4.setId(2L);
        department4.setParentDepartment(department3);
        savedDepartment2 = department4;



        DepartmentModel departmentModel1 = new DepartmentModel();
        departmentModel1.setName("Test 1 unsaved department");
        departmentModel1.setCommentary("Comment on test 1 unsaved department");
        unsavedDepartmentModel1 = departmentModel1;

        DepartmentModel departmentModel3 = new DepartmentModel();
        departmentModel3.setName("Test 1 saved department");
        departmentModel3.setCommentary("Comment on test 1 saved department");
        departmentModel3.setId(1L);
        savedDepartmentModel1 = departmentModel3;

        DepartmentModel departmentModel2 = new DepartmentModel();
        departmentModel2.setName("Test 2 unsaved department");
        departmentModel2.setCommentary("Comment on test 2 unsaved department");
        departmentModel2.setParentDepartment(departmentModel3);
        unsavedDepartmentModel2 = departmentModel2;

        DepartmentModel departmentModel4 = new DepartmentModel();
        departmentModel4.setName("Test 2 saved department");
        departmentModel4.setCommentary("Comment on test 2 saved department");
        departmentModel4.setId(2L);
        departmentModel4.setParentDepartment(departmentModel3);
        savedDepartmentModel2 = departmentModel4;

    }

    @Test
    public void getDepartmentByIdTest() throws Exception {

        when(departmentService.getDepartmentById(2L)).thenReturn(savedDepartment2);
        when(departmentService.getDepartmentById(3L)).thenThrow(NotFoundException.class);
        when(castDomainToModel.departmentToModel(savedDepartment2)).thenReturn(savedDepartmentModel2);

        mockMvc.perform(get("/departments/3"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("customErrorMessage"))
                .andExpect(model().attributeExists("errorMessage"));
        verify(departmentService, times(1)).getDepartmentById(3L);
        verifyNoInteractions(castDomainToModel);

        mockMvc.perform(get("/departments/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/departments/department"))
                .andExpect(model().attribute("department", savedDepartmentModel2));

        verify(departmentService, times(1)).getDepartmentById(2L);
        verify(castDomainToModel, times(1)).departmentToModel(savedDepartment2);

    }

    @Test
    public void getAllDepartmentsTest() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(List.of(savedDepartment1, savedDepartment2));
        when(castDomainToModel.departmentToModel(savedDepartment1)).thenReturn(savedDepartmentModel1);
        when(castDomainToModel.departmentToModel(savedDepartment2)).thenReturn(savedDepartmentModel2);

        mockMvc.perform(get("/departments/getAll"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/departments/departmentsTable"))
                .andExpect(model().attribute("departmentsList", hasSize(2)))
                .andExpect(model().attribute("departmentsList", hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("commentary"),
                                hasProperty("name", is("Test 2 saved department")),
                                hasProperty("parentDepartment", is(savedDepartmentModel1))
                        )
                )));
        verify(departmentService, times(1)).getAllDepartments();
        verify(castDomainToModel, times(1)).departmentToModel(savedDepartment1);
        verify(castDomainToModel, times(1)).departmentToModel(savedDepartment2);
    }

    @Test
    public void createDepartmentFormTest() throws Exception {

        mockMvc.perform(get("/departments/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/departments/departmentCreate"))
                .andExpect(model().attributeExists("newDepartment"))
                .andExpect(model().attributeExists("departmentsList"));
    }

    @Test
    public void createDepartmentTest() throws Exception {
        when(castDomainToModel.departmentModelToDomain(any())).thenReturn(unsavedDepartment1);
        when(departmentService.createDepartment(unsavedDepartment1)).thenReturn(savedDepartment1);
        when(castDomainToModel.departmentToModel(savedDepartment1)).thenReturn(savedDepartmentModel1);

        mockMvc.perform(post("/departments/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Test unsaved department model")
                .param("commentary", "")
                .sessionAttr("newDepartment", new DepartmentModel()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/departments/1"))
                .andExpect(model().attribute("department",
                        hasProperty("name", is("Test 1 saved department"))));

        mockMvc.perform(post("/departments/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Te")
                .param("commentary", "")
                .sessionAttr("newDepartment", new DepartmentModel()))
                .andExpect(model().attributeHasFieldErrors("newDepartment", "name"));
    }

    @Test
    public void updateDepartmentFormTest() throws Exception {
        when(departmentService.getDepartmentById(2L)).thenReturn(savedDepartment2);
        when(castDomainToModel.departmentToModel(savedDepartment2)).thenReturn(savedDepartmentModel2);

        mockMvc.perform(get("/departments/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/departments/departmentUpdate"))
                .andExpect(model().attributeExists("department"))
                .andExpect(model().attributeExists("departmentsList"));

    }

    @Test
    public void updateProfessionTest() throws Exception {
        Department updatedD = new Department();
        updatedD.setName("Test updated department");
        updatedD.setCommentary("Comment on test updated department");
        updatedD.setId(1L);

        DepartmentModel updatedDM = new DepartmentModel();
        updatedDM.setName("Test updated department model");
        updatedDM.setCommentary("Comment on Test updated department");
        updatedDM.setId(1L);

        when(castDomainToModel.departmentModelToDomain(any())).thenReturn(updatedD);
        when(departmentService.updateDepartment(updatedD)).thenReturn(updatedD);
        when(castDomainToModel.departmentToModel(updatedD)).thenReturn(updatedDM);

        mockMvc.perform(post("/departments/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "Test updated department model")
                .param("commentary", "Comment on Test updated department")
                .sessionAttr("department", new DepartmentModel()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/departments/1"))
                .andExpect(model().attribute("department",
                        hasProperty("name", is("Test updated department model"))));

        mockMvc.perform(post("/departments/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Te")
                .param("commentary", "")
                .sessionAttr("department", new DepartmentModel()))
                .andExpect(model().attributeHasFieldErrors("department", "name"));
    }

    @Test
    public void deleteDepartmentByIdTest() throws Exception {
        mockMvc.perform(get("/departments/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/departments/getAll"));
    }
}
