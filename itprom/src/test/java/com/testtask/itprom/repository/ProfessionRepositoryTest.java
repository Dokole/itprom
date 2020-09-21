package com.testtask.itprom.repository;


import com.google.inject.internal.util.Lists;
import com.testtask.itprom.domain.Department;
import com.testtask.itprom.domain.Profession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProfessionRepositoryTest {

    @Autowired
    private ProfessionRepository professionRepository;
    private Profession testProfession;
    private Profession secondTestProfession;

    @BeforeEach
    public void init() {
        Profession profession1 = new Profession();
        profession1.setName("Test 1 profession");
        profession1.setCommentary("Comment on test 1 profession");
        testProfession = professionRepository.save(profession1);

        Profession profession2 = new Profession();
        profession2.setName("Test 2 profession");
        profession2.setCommentary("Comment on test 2 profession");
        secondTestProfession = professionRepository.save(profession2);

        Assert.isTrue(Lists.newArrayList(professionRepository.findAll()).size() == 2,
                "All professions weren't saved");
    }

    @AfterEach
    public void cleaning() {
        professionRepository.deleteAll();
        Assert.isTrue(Lists.newArrayList(professionRepository.findAll()).size() == 0,
                "All professions weren't deleted");
    }

    @Test
    public void saveProfessionTest() {
        Assert.notNull(testProfession.getId(), "Profession wasn't saved");
        Assert.notNull(secondTestProfession.getId(), "Profession wasn't saved");
    }
    @Test
    public void updateProfessionTest() {
        testProfession.setName("changed name");
        testProfession = professionRepository.save(testProfession);
        Assert.isTrue(testProfession.getName().equals("changed name"), "Profession wasn't updated");
    }

    @Test
    public void getProfessionByIdTest() {
        Profession profession = professionRepository.findById(testProfession.getId()).orElse(null);
        Assert.notNull(profession, "Profession wasn't found in the database");
    }

    @Test
    public void getAllProfessionsTest() {
        List<Profession> professions = Lists.newArrayList(professionRepository.findAll());
        Assert.isTrue(professions.size() == 2, "All professions weren't received");
    }
    @Test
    public void deleteAllProfessionsTest() {
        Assert.isTrue(Lists.newArrayList(professionRepository.findAll()).size() == 2,
                "All departments weren't received");
        professionRepository.deleteAll();
        Assert.isTrue(Lists.newArrayList(professionRepository.findAll()).size() == 0,
                "All departments weren't received");
    }
    @Test
    public void deleteProfessionByIdTest() {
        Long id = secondTestProfession.getId();
        professionRepository.deleteById(id);
        Assert.isNull(professionRepository.findById(id).orElse(null), "Profession wasn't deleted");
        Assert.isTrue(Lists.newArrayList(professionRepository.findAll()).size() == 1,
                "Number of professions wasn't changed by only 1 object");
        id = testProfession.getId();
        professionRepository.deleteById(id);
        Assert.isTrue(Lists.newArrayList(professionRepository.findAll()).size() == 0,
                "Professions still exist");
    }
}
