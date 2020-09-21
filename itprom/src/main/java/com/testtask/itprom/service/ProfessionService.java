package com.testtask.itprom.service;

import com.testtask.itprom.domain.Department;
import com.testtask.itprom.domain.Employee;
import com.testtask.itprom.domain.Profession;

import java.util.List;

public interface ProfessionService {

    Profession getProfessionById(Long id);

    List<Profession> getAllProfessions();

    Profession createProfession(Profession profession);

    Profession updateProfession(Profession profession);

    void deleteProfessionById(Long id);

    void deleteAllProfessions();
}
