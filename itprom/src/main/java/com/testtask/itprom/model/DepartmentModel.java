package com.testtask.itprom.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class DepartmentModel extends BaseEntity {

    @NotBlank(message = "Department must have a name")
    @Size(min = 3, max = 100, message = "Must be at least 3 characters long and less than 100")
    protected String name;

    protected DepartmentModel parentDepartment;

    protected transient Long parentDepartmentId;

    public DepartmentModel() {
    }

    public DepartmentModel getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(DepartmentModel parentDepartmentModel) {
        this.parentDepartment = parentDepartmentModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void setParentDepartmentId(Long parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId;
    }

    public static DepartmentModel nullValueDepartment() {
        DepartmentModel departmentModel = new DepartmentModel();
        departmentModel.setName("none");
        return departmentModel;
    }

    @Override
    public String toString() {
        return "DepartmentModel{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", commentary='" + commentary + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentModel that = (DepartmentModel) o;
        return name.equals(that.name) &&
                Objects.equals(parentDepartment, that.parentDepartment) &&
                Objects.equals(parentDepartmentId, that.parentDepartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parentDepartment);
    }
}
