package dev.kolyapetrov.mypracticewithyandextask.service;

import dev.kolyapetrov.mypracticewithyandextask.entity.Citizen;
import dev.kolyapetrov.mypracticewithyandextask.entity.Import;
import dev.kolyapetrov.mypracticewithyandextask.exception_handling.IncorrectDataException;
import dev.kolyapetrov.mypracticewithyandextask.repository.ImportRepository;
import dev.kolyapetrov.mypracticewithyandextask.validation.CitizenPatch;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

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
        validateObject(importData);
    }

    private void validateCitizen(Citizen citizen) {
        validateObject(citizen, CitizenPatch.class);
    }

    private void validateObject(Object object, Class<?>... groupValidation) {
        var violations = validator.validate(object, groupValidation);
        if (!violations.isEmpty()) {
            List<String> listOfErrors = new ArrayList<>();
            violations.forEach(err -> listOfErrors.add(err.getMessage()));
            throw new IncorrectDataException(listOfErrors);
        }
    }

    private void changingRelativesOfCitizens(Citizen citizenFromDB, List<Citizen> citizens) {
        citizenFromDB.getRelatives().forEach(
                relative -> {
                    Citizen citizen = citizens.stream().filter(citizen_ ->
                                    Objects.equals(citizen_.getCitizen_id(), relative))
                            .findFirst().orElse(null);
                    assert citizen != null;
                    if (!citizen.getRelatives().contains(citizenFromDB.getCitizen_id())) {
                        citizen.getRelatives().add(citizenFromDB.getCitizen_id());
                    }
                }
        );
    }

    private void importDataFromJsonCitizenToDbCitizen(Citizen citizenFromDB, Citizen enteredCitizen) {
        Field[] fields = enteredCitizen.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(enteredCitizen) != null) {
                    Field field1 = citizenFromDB
                            .getClass()
                            .getDeclaredField(field.getName());
                    field1.setAccessible(true);
                    field1.set(citizenFromDB, field.get(enteredCitizen));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
    public Citizen editCitizen(Long importId, Long citizenId, Citizen enteredCitizen) {
        validateCitizen(enteredCitizen);

        Import myImport = importRepository.findByImportId(importId);
        if (myImport == null) {
            throw new IncorrectDataException(List.of("Incorrect import_id"));
        }

        List<Citizen> citizens = myImport.getCitizens();
        var optionalObj = citizens.stream().filter(citizen ->
                Objects.equals(citizen.getCitizen_id(), citizenId)).findFirst();

        Citizen citizenFromDB;
        if (optionalObj.isPresent()) {
            citizenFromDB = optionalObj.get();
        } else {
            throw new IncorrectDataException("Incorrect citizen_id");
        }

        Set<Long> relativesIds = new HashSet<>(enteredCitizen.getRelatives());
        if (relativesIds.size() != enteredCitizen.getRelatives().size()) {
            throw new IncorrectDataException("Relatives ids must be unique");
        }
        if (enteredCitizen.getRelatives().contains(enteredCitizen.getCitizen_id())) {
            throw new IncorrectDataException("Relatives ids must be unique");
        }

        importDataFromJsonCitizenToDbCitizen(citizenFromDB, enteredCitizen);

        changingRelativesOfCitizens(citizenFromDB, citizens);

        importRepository.save(myImport);

        return citizenFromDB;
    }

    @Override
    public List<Citizen> getCitizensByImportId(Long importId) {
        if (importId <= 0 || importRepository.count() < importId) {
            throw new IncorrectDataException("Incorrect import_id");
        }
        return importRepository.findByImportId(importId).getCitizens();
    }
}
