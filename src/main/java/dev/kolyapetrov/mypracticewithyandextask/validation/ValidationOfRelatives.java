package dev.kolyapetrov.mypracticewithyandextask.validation;

import dev.kolyapetrov.mypracticewithyandextask.entity.Citizen;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationOfRelatives
        implements ConstraintValidator<CheckRelatives, List<Citizen>> {

    private List<Citizen> citizens;
    private ConstraintValidatorContext context;
    private HashMap<Integer, Set<Integer>> dictionaryOfRelatives;

    @Override
    public boolean isValid(List<Citizen> citizens,
                           ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        // Dictionary of citizen_id and his relatives
        HashMap<Integer, Set<Integer>> dictionaryOfRelatives = new HashMap<>();
        citizens.forEach(citizen -> dictionaryOfRelatives
                .put(citizen.getCitizen_id(), new HashSet<>(citizen.getRelatives()))
        );

        this.context = context;
        this.citizens = citizens;
        this.dictionaryOfRelatives = dictionaryOfRelatives;

        return validateForUniqueCitizenIds() &
                validateForUniqueRelativesIds() &
                validateCitizenNotRelativeForHimself() &
                validateForRelativeExists() &
                validateForDuplexRelatives();
    }

    @Override
    public void initialize(CheckRelatives checkRelatives) {

    }

    private boolean validateForUniqueCitizenIds() {
        Set<Integer> citizensIds = citizens.stream()
                .map(Citizen::getCitizen_id)
                .collect(Collectors.toSet());

        if (citizensIds.size() != citizens.size()) {
            context.buildConstraintViolationWithTemplate("Citizens ids must be unique")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateForUniqueRelativesIds() {

        boolean isRelativesIdsUnique = citizens.stream().allMatch(citizen ->
                citizen.getRelatives().size() == dictionaryOfRelatives
                        .get(citizen.getCitizen_id()).size());

        if (!isRelativesIdsUnique) {
            context.buildConstraintViolationWithTemplate("Relatives ids must be unique")
                    .addConstraintViolation();
        }
        return isRelativesIdsUnique;
    }

    private boolean validateForRelativeExists() {

        boolean isRelativeExists = citizens.stream().allMatch(
                citizen -> citizen.getRelatives().stream().allMatch(
                        relative -> dictionaryOfRelatives.get(relative) != null)
        );

        if (!isRelativeExists) {
            context.buildConstraintViolationWithTemplate("Citizen relative doesn't exists")
                    .addConstraintViolation();
        }

        return isRelativeExists;
    }

    private boolean validateCitizenNotRelativeForHimself() {

        boolean isCitizenNotRelativeForHimself = citizens.stream().allMatch(
                citizen -> {
                    var relatives = dictionaryOfRelatives.get(citizen.getCitizen_id());
                    if (relatives != null) return !relatives.contains(citizen.getCitizen_id());
                    return true;
                });

        if (!isCitizenNotRelativeForHimself) {
            context.buildConstraintViolationWithTemplate("Citizen can not be relative to himself")
                    .addConstraintViolation();
        }
        return isCitizenNotRelativeForHimself;
    }

    private boolean validateForDuplexRelatives() {

        boolean isRelativesAreDuplex = citizens.stream().allMatch(
                citizen -> citizen.getRelatives().stream().allMatch(
                        relative -> {
                            var relatives = dictionaryOfRelatives.get(relative);
                            if (relatives != null) return relatives.contains(citizen.getCitizen_id());
                            return true;
                        }
                ));

        if (!isRelativesAreDuplex) {
            context.buildConstraintViolationWithTemplate("Citizen relatives must be duplex")
                    .addConstraintViolation();
        }

        return isRelativesAreDuplex;
    }
}
