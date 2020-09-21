package com.testtask.itprom.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    @Column(name = "first_name")
    protected String firstName;
    @Column(name = "last_name")
    protected String lastName;
    @Column(name = "patronymic")
    protected String patronymic;

    @ManyToOne
    @JoinColumn(name = "profession_id")
    protected Profession profession;

    @ManyToOne
    @JoinColumn(name = "department_id")
    protected Department department;

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

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return firstName.equals(employee.firstName) &&
                lastName.equals(employee.lastName) &&
                Objects.equals(patronymic, employee.patronymic) &&
                Objects.equals(profession, employee.profession) &&
                Objects.equals(department, employee.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, profession, department);
    }
}
