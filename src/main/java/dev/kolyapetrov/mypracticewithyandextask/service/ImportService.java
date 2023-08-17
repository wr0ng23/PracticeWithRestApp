package dev.kolyapetrov.mypracticewithyandextask.service;

import dev.kolyapetrov.mypracticewithyandextask.entity.Citizen;
import dev.kolyapetrov.mypracticewithyandextask.entity.Import;

import java.util.List;

public interface ImportService {
    Long saveImportData(Import importData);
    Citizen editCitizen(Integer importId, Integer citizenId, Citizen eteredCitizen);

    List<Citizen> getCitizensByImportId(Long importId);
}
