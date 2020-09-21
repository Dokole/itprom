package com.testtask.itprom.controller;


import com.testtask.itprom.domain.Profession;
import com.testtask.itprom.model.ProfessionModel;
import com.testtask.itprom.service.ProfessionService;
import com.testtask.itprom.util.CastDomainToModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/professions")
public class ProfessionControllerImp implements ProfessionController {

    private final CastDomainToModel castDomainToModel;
    private final ProfessionService professionService;

    @Autowired
    public ProfessionControllerImp(CastDomainToModel castDomainToModel, ProfessionService professionService) {
        this.castDomainToModel = castDomainToModel;
        this.professionService = professionService;
    }

    @Override
    @GetMapping("/{id}")
    public String getProfessionById(@PathVariable Long id, Model model) {
        Profession profession = professionService.getProfessionById(id);
        ProfessionModel professionModel = castDomainToModel.professionToModel(profession);
        model.addAttribute("profession", professionModel);
        return "/database/professions/profession";
    }

    @Override
    @GetMapping("/getAll")
    public String getAllProfessions(Model model) {
        List<Profession> professions = professionService.getAllProfessions();
        List<ProfessionModel> professionModels = professions.stream()
                .map(castDomainToModel::professionToModel).collect(Collectors.toList());
        model.addAttribute("professionsList", professionModels);
        return "/database/professions/professionsTable";
    }

    @Override
    @GetMapping("/create")
    public String createProfessionForm(Model model) {
        if (!model.containsAttribute("newProfession")) {
            model.addAttribute("newProfession", new ProfessionModel());
        }
        return "/database/professions/professionCreate";
    }

    @Override
    @PostMapping("/create")
    public String createProfession(@Valid @ModelAttribute("newProfession") ProfessionModel professionModel,
                                   Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "/database/professions/professionCreate";
        }
        Profession profession = castDomainToModel.professionModelToDomain(professionModel);
        profession = professionService.createProfession(profession);
        ProfessionModel createdProfession = castDomainToModel.professionToModel(profession);

        model.addAttribute("profession", createdProfession);
        return "redirect:/professions/" + createdProfession.getId();
    }

    @Override
    @GetMapping("/{id}/update")
    public String updateProfessionForm(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("profession")) {
            ProfessionModel professionModel = castDomainToModel.professionToModel(professionService.getProfessionById(id));
            model.addAttribute("profession", professionModel);
        }
        return "/database/professions/professionUpdate";
    }

    @Override
    @PostMapping("/update")
    public String updateProfession(@Valid @ModelAttribute("profession") ProfessionModel professionModel, Errors errors,
                                    Model model) {
        if (errors.hasErrors()) {
            return "/database/professions/professionUpdate";
        }
        Profession profession = castDomainToModel.professionModelToDomain(professionModel);
        profession = professionService.updateProfession(profession);
        ProfessionModel updatedProfession = castDomainToModel.professionToModel(profession);

        model.addAttribute("profession", updatedProfession);
        return "redirect:/professions/" + updatedProfession.getId();
    }

    @Override
    @GetMapping("/{id}/delete")
    public String deleteProfessionById(@PathVariable Long id, Model model) {
        professionService.deleteProfessionById(id);
        return "redirect:/professions/getAll";
    }
}
