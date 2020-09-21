package com.testtask.itprom;

import com.testtask.itprom.domain.Department;
import com.testtask.itprom.domain.Employee;
import com.testtask.itprom.domain.Profession;
import com.testtask.itprom.model.DepartmentModel;
import com.testtask.itprom.model.EmployeeModel;
import com.testtask.itprom.model.ProfessionModel;
import com.testtask.itprom.util.CastDomainToModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CastDomainToModel.class)
public class CastDomainToModelTest {

    @Autowired
    private CastDomainToModel castDomainToModel;

    private static Employee savedEmployeeWithoutRelated;
    private static EmployeeModel savedEmployeeModelWithoutRelated;
    private static Employee savedEmployeeWithRelated;
    private static EmployeeModel savedEmployeeModelWithRelated;

    private static Department savedDepartmentParent;
    private static DepartmentModel savedDepartmentModelParent;
    private static Department savedDepartmentChild;
    private static DepartmentModel savedDepartmentModelChild;

    private static Profession savedProfession;
    private static ProfessionModel savedProfessionModel;

    @BeforeAll
    public static void init() {
        //DEPS
        Department departmentParent = new Department();
        departmentParent.setName("Test 1 saved department");
        departmentParent.setCommentary("Comment on test 1 saved department");
        departmentParent.setId(1L);
        savedDepartmentParent = departmentParent;

        DepartmentModel departmentModel3 = new DepartmentModel();
        departmentModel3.setName("Test 1 saved department");
        departmentModel3.setCommentary("Comment on test 1 saved department");
        departmentModel3.setId(1L);
        savedDepartmentModelParent = departmentModel3;

        Department department4 = new Department();
        department4.setName("Test 2 saved department");
        department4.setCommentary("Comment on test 2 saved department");
        department4.setId(2L);
        department4.setParentDepartment(savedDepartmentParent);
        savedDepartmentChild = department4;

        DepartmentModel departmentModel4 = new DepartmentModel();
        departmentModel4.setName("Test 2 saved department");
        departmentModel4.setCommentary("Comment on test 2 saved department");
        departmentModel4.setId(2L);
        departmentModel4.setParentDepartment(savedDepartmentModelParent);
        savedDepartmentModelChild = departmentModel4;


        //PROFS
        Profession savedP = new Profession();
        savedP.setName("Test saved profession");
        savedP.setCommentary("Comment on test saved profession");
        savedP.setId(1L);
        savedProfession = savedP;

        ProfessionModel savedPM = new ProfessionModel();
        savedPM.setName("Test saved profession");
        savedPM.setCommentary("Comment on test saved profession");
        savedPM.setId(1L);
        savedProfessionModel = savedPM;


        //EMPS
        Employee savedE = new Employee();
        savedE.setId(1L);
        savedE.setFirstName("Jon");
        savedE.setLastName("Doe");
        savedE.setPatronymic("Vas");
        savedE.setCommentary("Test employee saved");
        savedEmployeeWithoutRelated = savedE;

        EmployeeModel savedEM = new EmployeeModel();
        savedEM.setId(1L);
        savedEM.setFirstName("Jon");
        savedEM.setLastName("Doe");
        savedEM.setPatronymic("Vas");
        savedEM.setCommentary("Test employee saved");
        savedEmployeeModelWithoutRelated = savedEM;

        Employee savedE2 = new Employee();
        savedE2.setId(1L);
        savedE2.setFirstName("Jon");
        savedE2.setLastName("Doe");
        savedE2.setPatronymic("Vas");
        savedE2.setCommentary("Test employee saved");
        savedE2.setDepartment(savedDepartmentParent);
        savedE2.setProfession(savedProfession);
        savedEmployeeWithRelated = savedE2;

        EmployeeModel savedEm2 = new EmployeeModel();
        savedEm2.setId(1L);
        savedEm2.setFirstName("Jon");
        savedEm2.setLastName("Doe");
        savedEm2.setPatronymic("Vas");
        savedEm2.setCommentary("Test employee saved");
        savedEm2.setDepartment(savedDepartmentModelParent);
        savedEm2.setProfession(savedProfessionModel);
        savedEmployeeModelWithRelated = savedEm2;
    }

    @Test
    public void professionDomainToModelCastTest() {
        ProfessionModel resultToModel = castDomainToModel.professionToModel(savedProfession);
        Assert.isTrue(resultToModel.equals(savedProfessionModel), "Profession cast from domain to model went wrong");
    }

    @Test
    public void professionModelToDomainCastTest() {
        Profession resultToDomain = castDomainToModel.professionModelToDomain(savedProfessionModel);
        Assert.isTrue(resultToDomain.equals(savedProfession), "Profession cast from model to domain went wrong");
    }

    @Test
    public void departmentDomainToModelCastTest() {
        DepartmentModel resultToModel = castDomainToModel.departmentToModel(savedDepartmentParent);
        Assert.isTrue(resultToModel.equals(savedDepartmentModelParent), "Department parent cast from domain to model went wrong");

        resultToModel = castDomainToModel.departmentToModel(savedDepartmentChild);
        Assert.isTrue(resultToModel.equals(savedDepartmentModelChild), "Department child cast from domain to model went wrong");
    }

    @Test
    public void departmentModelToDomainCastTest() {
        Department resultToDomain = castDomainToModel.departmentModelToDomain(savedDepartmentModelParent);
        Assert.isTrue(resultToDomain.equals(savedDepartmentParent), "Department parent cast from model to domain went wrong");

        resultToDomain = castDomainToModel.departmentModelToDomain(savedDepartmentModelChild);
        Assert.isTrue(resultToDomain.equals(savedDepartmentChild), "Department child cast from model to domain went wrong");
    }


    @Test
    public void employeeDomainToModelCastTest () {
        EmployeeModel resultToModel = castDomainToModel.employeeToModel(savedEmployeeWithoutRelated);
        Assert.isTrue(resultToModel.equals(savedEmployeeModelWithoutRelated), "Employee without related cast from domain to model went wrong");

        resultToModel = castDomainToModel.employeeToModel(savedEmployeeWithRelated);
        Assert.isTrue(resultToModel.equals(savedEmployeeModelWithRelated), "Employee with related cast from domain to model went wrong");
    }

    @Test
    public void employeeModelToDomainCastTest() {
        Employee resultToDomain = castDomainToModel.employeeModelToDomain(savedEmployeeModelWithoutRelated);
        Assert.isTrue(resultToDomain.equals(savedEmployeeWithoutRelated), "Employee without related cast from model to domain went wrong");

        resultToDomain = castDomainToModel.employeeModelToDomain(savedEmployeeModelWithRelated);
        Assert.isTrue(resultToDomain.equals(savedEmployeeWithRelated), "Employee with related cast from model to domain went wrong");
    }

}
