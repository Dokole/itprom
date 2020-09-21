package com.testtask.itprom.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class EmployeeModel extends BaseEntity {

    @NotBlank(message = "Profession must have a firest name")
    @Size(min = 3, max = 50, message = "Must be at least 3 characters long and less than 50")
    protected String firstName;

    @NotBlank(message = "Profession must have a last name")
    @Size(min = 3, max = 50, message = "Must be at least 3 characters long and less than 50")
    protected String lastName;

    protected String patronymic;

    protected ProfessionModel profession;

    protected transient Long professionId;

    protected DepartmentModel department;

    protected transient Long departmentId;

    public EmployeeModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public ProfessionModel getProfession() {
        return profession;
    }

    public void setProfession(ProfessionModel professionModel) {
        this.profession = professionModel;
    }

    public DepartmentModel getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentModel departmentModel) {
        this.department = departmentModel;
    }

    public Long getProfessionId() {
        return professionId;
    }

    public void setProfessionId(Long professionId) {
        this.professionId = professionId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }


    @Override
    public String toString() {
        return "EmployeeModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", id=" + id +
                ", commentary='" + commentary + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeModel that = (EmployeeModel) o;
        return firstName.equals(that.firstName) &&
                lastName.equals(that.lastName) &&
                Objects.equals(patronymic, that.patronymic) &&
                Objects.equals(profession, that.profession) &&
                Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, profession, department);
    }
}
