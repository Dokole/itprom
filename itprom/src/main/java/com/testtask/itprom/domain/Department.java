package com.testtask.itprom.domain;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "departments")
public class Department extends BaseEntity {

    @Column(name = "name")
    protected String name;

    @ManyToOne
    @JoinColumn(name = "parent_department")
    protected Department parentDepartment;

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return name.equals(that.name) &&
                Objects.equals(parentDepartment, that.parentDepartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parentDepartment);
    }
}
