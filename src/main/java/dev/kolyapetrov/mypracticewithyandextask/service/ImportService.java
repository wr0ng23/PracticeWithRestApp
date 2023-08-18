package dev.kolyapetrov.mypracticewithyandextask.service;

import dev.kolyapetrov.mypracticewithyandextask.entity.Citizen;
import dev.kolyapetrov.mypracticewithyandextask.entity.Import;

import java.util.List;

public interface ImportService {
    Long saveImportData(Import importData);
    Citizen editCitizen(Long importId, Long citizenId, Citizen eteredCitizen);

    List<Citizen> getCitizensByImportId(Long importId);
}
