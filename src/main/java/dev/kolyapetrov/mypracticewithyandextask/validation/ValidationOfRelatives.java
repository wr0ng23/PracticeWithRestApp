package dev.kolyapetrov.mypracticewithyandextask.validation;

import dev.kolyapetrov.mypracticewithyandextask.entity.Citizen;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationOfRelatives
        implements ConstraintValidator<CheckRelatives, List<Citizen>> {

    @Override
    public boolean isValid(List<Citizen> citizens,
                           ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        Set<Integer> citizensIds = citizens.stream()
                .map(Citizen::getCitizen_id)
                .collect(Collectors.toSet());
        if (citizensIds.size() != citizens.size()) {
            context.buildConstraintViolationWithTemplate("Citizens ids must be unique")
                    .addConstraintViolation();
            return false;
        }

        HashMap<Integer, List<Integer>> dictionaryOfRelatives = new HashMap<>();
        // Putting key-value for citizen_id and relatives
        citizens.forEach(citizen -> dictionaryOfRelatives
                .put(citizen.getCitizen_id(), citizen.getRelatives())
        );

        // Validation of duplex relatives
        boolean flag = citizens.stream().allMatch(
                citizen -> citizen.getRelatives().stream().allMatch(
                        relative -> dictionaryOfRelatives.get(relative)
                                .contains(citizen.getCitizen_id())
                ));

        if (!flag) {
            context.buildConstraintViolationWithTemplate("Citizen relatives must be duplex")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(CheckRelatives checkRelatives) {

    }
}
