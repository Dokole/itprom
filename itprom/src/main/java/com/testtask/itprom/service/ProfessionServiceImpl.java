package com.testtask.itprom.service;

import com.google.inject.internal.util.Lists;
import com.testtask.itprom.domain.Profession;
import com.testtask.itprom.exceptions.BadRequestException;
import com.testtask.itprom.exceptions.NotFoundException;
import com.testtask.itprom.repository.ProfessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = "professions")
public class ProfessionServiceImpl implements ProfessionService {

    Logger logger = LoggerFactory.getLogger(ProfessionServiceImpl.class);

    private final ProfessionRepository professionRepository;

    @Autowired
    public ProfessionServiceImpl(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    @Override
    @Cacheable(key = "#root.method.name.concat(#id)")
    @Transactional(readOnly = true)
    public Profession getProfessionById(Long id) {
        Profession profession = professionRepository.findById(id).orElse(null);
        if (profession == null) {
            throw new NotFoundException("No profession found by id=" + id);
        }
        return profession;
    }

    @Override
    @Cacheable(key = "#root.method.name")
    @Transactional(readOnly = true)
    public List<Profession> getAllProfessions() {
        List<Profession> professions = Lists.newArrayList(professionRepository.findAll());
        if (professions == null || professions.isEmpty()) {
           return new ArrayList<>();
        }
        return professions;
    }

    @Override
    @CacheEvict(cacheNames = {"professions", "employees"}, allEntries = true)
    public Profession createProfession(Profession profession) {
        if (profession.getId() != null) {
            throw new BadRequestException("Id=" + profession.getId() + " should be null to create a profession. Can't be saved.");
        }
        return professionRepository.save(profession);
    }

    @Override
    @CacheEvict(cacheNames = {"professions", "employees"}, allEntries = true)
    public Profession updateProfession(Profession profession) {
        if (!professionRepository.existsById(profession.getId())) {
            throw new BadRequestException("Can't update profession with id=" + profession.getId() +
                    ". It doesn't exists, need to create it first.");
        }
        return professionRepository.save(profession);
    }

    @Override
    @CacheEvict(cacheNames = {"professions", "employees"}, allEntries = true)
    public void deleteProfessionById(Long id) {
        professionRepository.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = {"professions", "employees"}, allEntries = true)
    public void deleteAllProfessions() {
        professionRepository.deleteAll();
    }
}
