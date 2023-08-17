package dev.kolyapetrov.mypracticewithyandextask.service;

import dev.kolyapetrov.mypracticewithyandextask.entity.Citizen;
import dev.kolyapetrov.mypracticewithyandextask.entity.Import;
import dev.kolyapetrov.mypracticewithyandextask.exception_handling.IncorrectDataException;
import dev.kolyapetrov.mypracticewithyandextask.repository.ImportRepository;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportServiceImpl implements ImportService {
    Validator validator;
    ImportRepository importRepository;

    @Autowired
    public ImportServiceImpl(Validator validator, ImportRepository importRepository) {
        this.validator = validator;
        this.importRepository = importRepository;
    }

    private void validateImportData(Import importData) {
        var violations = validator.validate(importData);
        if (!violations.isEmpty()) {
            List<String> listOfErrors = new ArrayList<>();
            violations.forEach(err -> listOfErrors.add(err.getMessage()));
            throw new IncorrectDataException(listOfErrors);
        }
    }

    @Override
    public Long saveImportData(Import importData) {
        validateImportData(importData);

        importData.setImportId(importRepository.count() + 1);
        importRepository.save(importData);

        return importData.getImportId();
    }

    @Override
    public Citizen editCitizen(Integer importId, Integer citizenId, Citizen eteredCitizen) {
        return null;
    }

    @Override
    public List<Citizen> getCitizensByImportId(Long importId) {
        if (importId <= 0 || importRepository.count() < importId) {
            throw new IncorrectDataException(List.of("Incorrect import_id"));
        }
        return importRepository.findByImportId(importId).getCitizens();
    }
}
