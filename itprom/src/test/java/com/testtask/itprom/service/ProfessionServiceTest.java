package com.testtask.itprom.service;

import com.testtask.itprom.domain.Profession;
import com.testtask.itprom.exceptions.BadRequestException;
import com.testtask.itprom.exceptions.NotFoundException;
import com.testtask.itprom.repository.ProfessionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
public class ProfessionServiceTest {

    @Mock
    private ProfessionRepository professionRepository;

    @InjectMocks
    private ProfessionServiceImpl professionService;

    private static Profession savedProfession;
    private static Profession unsavedProfession;

    @BeforeAll
    private static void init() {
        Profession unsavedP = new Profession();
        unsavedP.setName("Test unsaved profession");
        unsavedP.setCommentary("Comment on test unsaved profession");
        unsavedProfession = unsavedP;

        Profession savedP = new Profession();
        savedP.setName("Test saved profession");
        savedP.setCommentary("Comment on test saved profession");
        savedP.setId(1L);
        savedProfession = savedP;
    }

    @Test
    public void getProfessionByIdTest() {
        when(professionRepository.findById(1L)).thenReturn(Optional.of(savedProfession));
        when(professionRepository.findById(3L)).thenReturn(Optional.empty());

        Assert.isTrue(professionService.getProfessionById(1L).equals(savedProfession), "Error while getting profession by id");
        Assertions.assertThrows(NotFoundException.class, () -> professionService.getProfessionById(3L));
    }

    @Test
    public void getAllProfessionsTest() {
        when(professionRepository.findAll()).thenReturn(List.of(savedProfession));
        Assert.isTrue(professionService.getAllProfessions().size() == 1, "Error while getting all professions");
    }

    @Test
    public void getAllProfessions_ErrorTest() {
        when(professionRepository.findAll()).thenReturn(List.of());
        Assert.isTrue(professionService.getAllProfessions().size() == 0,
                "Error while testing getting all professions as an empty list");
    }

    @Test
    public void createProfessionTest() {
        when(professionRepository.save(unsavedProfession)).thenReturn(savedProfession);
        Profession result = professionService.createProfession(unsavedProfession);
        Assert.isTrue(result.equals(savedProfession), "Profession wasn't saved");
    }

    @Test
    public void createProfession_WithExistedIdTest() {
        when(professionRepository.save(savedProfession)).thenReturn(savedProfession);
        Assertions.assertThrows(BadRequestException.class, () -> professionService.createProfession(savedProfession));
        verifyNoInteractions(professionRepository);
    }

    @Test
    public void updateProfessionTest() {
        when(professionRepository.save(savedProfession)).thenReturn(savedProfession);
        when(professionRepository.existsById(savedProfession.getId())).thenReturn(true);
        Profession result = professionService.updateProfession(savedProfession);
        Assert.isTrue(result.equals(savedProfession), "Profession wasn't updated");
    }

    @Test
    public void updateProfession_WithoutExistedIdTest() {
        when(professionRepository.save(savedProfession)).thenReturn(savedProfession);
        when(professionRepository.existsById(savedProfession.getId())).thenReturn(false);
        Assertions.assertThrows(BadRequestException.class, () -> professionService.updateProfession(savedProfession));
        verify(professionRepository, never()).save(savedProfession);
    }
}
