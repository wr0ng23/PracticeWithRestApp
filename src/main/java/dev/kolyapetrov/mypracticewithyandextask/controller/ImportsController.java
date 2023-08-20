package dev.kolyapetrov.mypracticewithyandextask.controller;

import dev.kolyapetrov.mypracticewithyandextask.dto.CitizenPresents;
import dev.kolyapetrov.mypracticewithyandextask.entity.Citizen;
import dev.kolyapetrov.mypracticewithyandextask.entity.Import;
import dev.kolyapetrov.mypracticewithyandextask.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/imports")
public class ImportsController {
    ImportService importService;

    @Autowired
    public ImportsController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping
    public ResponseEntity<HashMap<String, Long>> getImports(@RequestBody Import importOfCitizens) {

        Long importId = importService.saveImportData(importOfCitizens);

        HashMap<String, Long> importResponse = new HashMap<>();
        importResponse.put("import_id", importId);

        return new ResponseEntity<>(importResponse, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/{import_id}/citizens")
    public List<Citizen> getAllCitizens(@PathVariable Long import_id) {
        return importService.getCitizensByImportId(import_id);
    }

    @PatchMapping("/{import_id}/citizens/{citizen_id}")
    public ResponseEntity<Citizen> editCitizen(@PathVariable Long citizen_id,
                                               @PathVariable Long import_id,
                                               @RequestBody Citizen citizen) {

        Citizen pathcedCitizen = importService.editCitizen(import_id, citizen_id, citizen);

        return new ResponseEntity<>(pathcedCitizen, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{import_id}/citizens/birthdays")
    public HashMap<Long, List<CitizenPresents>> getBirthMonthsAndPresents(
            @PathVariable Long import_id) {

        return importService.getBirthdays(import_id);
    }
}

