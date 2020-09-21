package com.testtask.itprom.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class ProfessionModel extends BaseEntity {

    @NotBlank(message = "Profession must have a name")
    @Size(min = 3, max = 50, message = "Must be at least 3 characters long and less than 50")
    protected String name;

    public ProfessionModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ProfessionModel nullValueProfession() {
        ProfessionModel professionModel = new ProfessionModel();
        professionModel.setName("none");
        return professionModel;
    }

    @Override
    public String toString() {
        return "ProfessionModel{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", commentary='" + commentary + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfessionModel that = (ProfessionModel) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
