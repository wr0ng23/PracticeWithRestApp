package dev.kolyapetrov.mypracticewithyandextask.controller;

import dev.kolyapetrov.mypracticewithyandextask.entity.Citizen;
import dev.kolyapetrov.mypracticewithyandextask.entity.Import;
import dev.kolyapetrov.mypracticewithyandextask.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<HashMap<String, Long>> getImports(
            @RequestBody Import importOfCitizens) {

        Long importId = importService.saveImportData(importOfCitizens);

        HashMap<String, Long> importResponse = new HashMap<>();
        importResponse.put("import_id", importId);

        return new ResponseEntity<>(importResponse, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/{import_id}/citizens")
    public HashMap<String, List<Citizen>> getAllCitizens(@PathVariable Long import_id) {
        HashMap<String, List<Citizen>> response = new HashMap<>();
        response.put("data", importService.getCitizensByImportId(import_id));

        return response;
    }

    @PatchMapping("/{import_id}/citizens/{citizen_id}")
    public ResponseEntity<Citizen> editCitizen(@PathVariable Long citizen_id,
                                               @PathVariable Long import_id,
                                               @RequestBody Citizen citizen) {

        return null;
    }
}

