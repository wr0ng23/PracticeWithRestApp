package dev.kolyapetrov.mypracticewithyandextask.controller;

import dev.kolyapetrov.mypracticewithyandextask.entity.Import;
import dev.kolyapetrov.mypracticewithyandextask.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/imports")
public class ImportsController {
    ImportService citizenService;

    @Autowired
    public ImportsController(ImportService citizenService) {
        this.citizenService = citizenService;
    }

    @PostMapping
    public ResponseEntity<HashMap<String, Long>> getImports(
            @RequestBody Import importOfCitizens) {

        var importResponse = citizenService.saveImportData(importOfCitizens);

        return new ResponseEntity<>(importResponse, HttpStatusCode.valueOf(201));
    }
}

