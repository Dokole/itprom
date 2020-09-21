package com.testtask.itprom.controller;

import com.testtask.itprom.model.ProfessionModel;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

public interface ProfessionController {

    String getProfessionById(Long id, Model model);

    String getAllProfessions(Model model);

    String createProfessionForm(Model model);

    String createProfession(ProfessionModel professionModel, Errors errors, Model model);

    String updateProfessionForm(Long id, Model model);

    String updateProfession(ProfessionModel professionModel, Errors errors, Model model);

    String deleteProfessionById(Long id, Model model);
}
