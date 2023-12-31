package dev.kolyapetrov.mypracticewithyandextask.service;

import dev.kolyapetrov.mypracticewithyandextask.dto.CitizenPresents;
import dev.kolyapetrov.mypracticewithyandextask.dto.PercentilesByTown;
import dev.kolyapetrov.mypracticewithyandextask.entity.Citizen;
import dev.kolyapetrov.mypracticewithyandextask.entity.Import;
import dev.kolyapetrov.mypracticewithyandextask.exception_handling.IncorrectDataException;
import dev.kolyapetrov.mypracticewithyandextask.repository.ImportRepository;
import dev.kolyapetrov.mypracticewithyandextask.validation.group_of_validation.CitizenPatch;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    private void validateCitizen(Citizen citizen, Long citizenId, List<Citizen> citizens) {
        validateObject(citizen, CitizenPatch.class);
        citizen.setCitizen_id(citizenId);

        validateRelatives(citizen, citizens);
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
        if (citizenFromDB.getRelatives().isEmpty()) {
            citizens.forEach(citizen ->
                    citizen.getRelatives().remove(citizenFromDB.getCitizen_id())
            );
            return;
        }

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

    private void validateRelatives(Citizen enteredCitizen, List<Citizen> citizens) {
        if (enteredCitizen.getRelatives() == null) return;

        Set<Long> relativesIds = new HashSet<>(enteredCitizen.getRelatives());
        if (relativesIds.size() != enteredCitizen.getRelatives().size()) {
            throw new IncorrectDataException("Relatives ids must be unique");
        }
        if (enteredCitizen.getRelatives().contains(enteredCitizen.getCitizen_id())) {
            throw new IncorrectDataException("Citizen can not be relative to himself");
        }

        List<Long> citizenIds = citizens.stream().map(Citizen::getCitizen_id).toList();
        boolean isCorrectRelativeIds = new HashSet<>(citizenIds).containsAll(relativesIds);
        if (!isCorrectRelativeIds) {
            throw new IncorrectDataException("Incorrect relative id");
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
        Import myImport = importRepository.findByImportId(importId);
        if (myImport == null) {
            throw new IncorrectDataException("Incorrect import_id");
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

        validateCitizen(enteredCitizen, citizenId, citizens);

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

    public HashMap<Long, List<CitizenPresents>> getBirthdays(Long importId) {
        var citizens = this.getCitizensByImportId(importId);
        HashMap<Long, Integer> citizenIdAndMonthOfBirth = new HashMap<>();
        citizens.forEach(citizen ->
                citizenIdAndMonthOfBirth
                        .put(citizen.getCitizen_id(), citizen.getBirthDate().getMonthValue())

        );

        HashMap<Long, List<CitizenPresents>> birthdays = new HashMap<>();
        for (long i = 1; i <= 12; ++i) {
            birthdays.put(i, new ArrayList<>());

            for (var citizen : citizens) {
                var citizenPresents = new CitizenPresents();
                citizenPresents.setCitizenId(citizen.getCitizen_id());
                long j = 0;
                for (var relative : citizen.getRelatives()) {
                    if (citizenIdAndMonthOfBirth.get(relative) == i) {
                        citizenPresents.setPresents(++j);
                    }
                }
                if (citizenPresents.getPresents() != 0) {
                    birthdays.get(i).add(citizenPresents);
                }
            }
        }

        return birthdays;
    }

    public List<PercentilesByTown> getListOfTownsForPercentiles(Long importId) {
        var citizens = this.getCitizensByImportId(importId);

        Set<String> towns = new HashSet<>();
        citizens.forEach(citizen -> towns.add(citizen.getTown()));

        HashMap<String, List<Citizen>> townsAndCitizens = new HashMap<>();
        towns.forEach(town -> {
                    var citizensForTown = citizens.stream().filter(citizen ->
                            citizen.getTown().equals(town)).toList();
                    townsAndCitizens.put(town, citizensForTown);
                }
        );

        List<PercentilesByTown> listOfPercentilesForTowns = new ArrayList<>();
        townsAndCitizens.forEach((town, citizens_) -> {
                    List<Integer> ages = new ArrayList<>();
                    citizens_.forEach(citizen -> {
                                long yearsOld = ChronoUnit.YEARS.between(citizen.getBirthDate(), LocalDate.now());
                                ages.add((int) yearsOld);
                            }
                    );
                    ages.sort(Integer::compareTo);

                    PercentilesByTown percentilesByTown = getPercentilesByTown(town, ages);
                    listOfPercentilesForTowns.add(percentilesByTown);
                }
        );

        return listOfPercentilesForTowns;
    }

    private PercentilesByTown getPercentilesByTown(String town, List<Integer> ages) {
        int p50Index = (int) Math.round(ages.size() * 0.50) - 1;
        var p50 = ages.get(p50Index);

        int p75Index = (int) Math.round(ages.size() * 0.75) - 1;
        var p75 = ages.get(p75Index);

        int p99Index = (int) Math.round(ages.size() * 0.99) - 1;
        var p99 = ages.get(p99Index);

        PercentilesByTown percentilesByTown = new PercentilesByTown();

        percentilesByTown.setTown(town);
        percentilesByTown.setP50(p50);
        percentilesByTown.setP75(p75);
        percentilesByTown.setP99(p99);

        return percentilesByTown;
    }
}

