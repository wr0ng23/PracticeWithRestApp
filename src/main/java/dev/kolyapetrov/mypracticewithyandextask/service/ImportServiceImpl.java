package dev.kolyapetrov.mypracticewithyandextask.service;

import dev.kolyapetrov.mypracticewithyandextask.entity.Import;
import dev.kolyapetrov.mypracticewithyandextask.exception_handling.IncorrectDataException;
import dev.kolyapetrov.mypracticewithyandextask.repository.ImportRepository;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public HashMap<String, Long> saveImportData(Import importData) {

        var violations = validator.validate(importData);
        if (!violations.isEmpty()) {
            List<String> listOfErrors = new ArrayList<>();
            violations.forEach(err -> listOfErrors.add(err.getMessage()));
            throw new IncorrectDataException(listOfErrors);
        }

        importRepository.save(importData);

        HashMap<String, Long> importResponse = new HashMap<>();
        importResponse.put("import_id", importRepository.count());

        return importResponse;
    }
}
