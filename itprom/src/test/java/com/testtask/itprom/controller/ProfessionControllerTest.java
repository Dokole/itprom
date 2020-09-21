package com.testtask.itprom.controller;

import com.testtask.itprom.domain.Profession;
import com.testtask.itprom.exceptions.NotFoundException;
import com.testtask.itprom.model.ProfessionModel;
import com.testtask.itprom.service.ProfessionService;
import com.testtask.itprom.util.CastDomainToModel;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProfessionControllerImp.class)
@ExtendWith(SpringExtension.class)
public class ProfessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessionService professionService;
    @MockBean
    private CastDomainToModel castDomainToModel;

    @InjectMocks
    private ProfessionControllerImp professionController;

    private static Profession unsavedProfession;
    private static Profession savedProfession;
    private static ProfessionModel unsavedProfessionModel;
    private static ProfessionModel savedProfessionModel;
    @BeforeAll
    public static void init() {
        Profession unsavedP = new Profession();
        unsavedP.setName("Test unsaved profession");
        unsavedP.setCommentary("Comment on test unsaved profession");
        unsavedProfession = unsavedP;

        Profession savedP = new Profession();
        savedP.setName("Test saved profession");
        savedP.setCommentary("Comment on test saved profession");
        savedP.setId(1L);
        savedProfession = savedP;

        ProfessionModel unsavedPM = new ProfessionModel();
        unsavedPM.setName("Test unsaved profession");
        unsavedPM.setCommentary("Comment on Test unsaved profession");
        unsavedProfessionModel = unsavedPM;

        ProfessionModel savedPM = new ProfessionModel();
        savedPM.setName("Test saved profession model");
        savedPM.setCommentary("Comment on Test saved profession");
        savedPM.setId(1L);
        savedProfessionModel = savedPM;
    }

    @Test
    public void getProfessionByIdTest() throws Exception {

        when(professionService.getProfessionById(1L)).thenReturn(savedProfession);
        when(professionService.getProfessionById(2L)).thenThrow(NotFoundException.class);
        when(castDomainToModel.professionToModel(savedProfession)).thenReturn(savedProfessionModel);

        mockMvc.perform(get("/professions/2"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("customErrorMessage"))
                .andExpect(model().attributeExists("errorMessage"));
        verify(professionService, times(1)).getProfessionById(2L);
        verifyNoInteractions(castDomainToModel);

        mockMvc.perform(get("/professions/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/professions/profession"))
                .andExpect(model().attribute("profession", savedProfessionModel));

        verify(professionService, times(1)).getProfessionById(1L);
        verify(castDomainToModel, times(1)).professionToModel(savedProfession);

    }

    @Test
    public void getAllProfessionsTest() throws Exception {
        when(professionService.getAllProfessions()).thenReturn(Lists.newArrayList(savedProfession));
        when(castDomainToModel.professionToModel(savedProfession)).thenReturn(savedProfessionModel);

        mockMvc.perform(get("/professions/getAll"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/professions/professionsTable"))
                .andExpect(model().attribute("professionsList", hasSize(1)))
                .andExpect(model().attribute("professionsList", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("commentary"),
                                hasProperty("name", is("Test saved profession model"))
                        )
                )));
        verify(professionService, times(1)).getAllProfessions();
        verify(castDomainToModel, times(1)).professionToModel(savedProfession);

    }

    @Test
    public void createProfessionFormTest() throws Exception {

        mockMvc.perform(get("/professions/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/professions/professionCreate"))
                .andExpect(model().attributeExists("newProfession"));
    }

    @Test
    public void createProfessionTest() throws Exception {
        when(castDomainToModel.professionModelToDomain(any())).thenReturn(unsavedProfession);
        when(professionService.createProfession(unsavedProfession)).thenReturn(savedProfession);
        when(castDomainToModel.professionToModel(savedProfession)).thenReturn(savedProfessionModel);

        mockMvc.perform(post("/professions/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Test unsaved profession model")
                .param("commentary", "")
                .sessionAttr("newProfession", new ProfessionModel()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professions/1"))
                .andExpect(model().attribute("profession",
                        hasProperty("name", is("Test saved profession model"))));

        mockMvc.perform(post("/professions/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Te")
                .param("commentary", "")
                .sessionAttr("newProfession", new ProfessionModel()))
                .andExpect(model().attributeHasFieldErrors("newProfession", "name"));
    }

    @Test
    public void updateProfessionFormTest() throws Exception {
        when(professionService.getProfessionById(1L)).thenReturn(savedProfession);
        when(castDomainToModel.professionToModel(savedProfession)).thenReturn(savedProfessionModel);

        mockMvc.perform(get("/professions/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("/database/professions/professionUpdate"))
                .andExpect(model().attributeExists("profession"));

    }

    @Test
    public void updateProfessionTest() throws Exception {
        Profession updatedP = new Profession();
        updatedP.setName("Test updated profession");
        updatedP.setCommentary("Comment on test updated profession");
        updatedP.setId(1L);

        ProfessionModel updatedPM = new ProfessionModel();
        updatedPM.setName("Test updated profession model");
        updatedPM.setCommentary("Comment on Test updated profession");
        updatedPM.setId(1L);

        when(castDomainToModel.professionModelToDomain(any())).thenReturn(updatedP);
        when(professionService.updateProfession(updatedP)).thenReturn(updatedP);
        when(castDomainToModel.professionToModel(updatedP)).thenReturn(updatedPM);

        mockMvc.perform(post("/professions/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("name", "Test updated profession model")
                .param("commentary", "")
                .sessionAttr("profession", new ProfessionModel()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professions/1"))
                .andExpect(model().attribute("profession",
                        hasProperty("name", is("Test updated profession model"))));

        mockMvc.perform(post("/professions/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Te")
                .param("commentary", "")
                .sessionAttr("profession", new ProfessionModel()))
                .andExpect(model().attributeHasFieldErrors("profession", "name"));
    }

    @Test
    public void deleteProfessionByIdTest() throws Exception {
        mockMvc.perform(get("/professions/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professions/getAll"));
    }
}