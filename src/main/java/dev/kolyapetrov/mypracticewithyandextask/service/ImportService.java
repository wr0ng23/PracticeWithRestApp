package dev.kolyapetrov.mypracticewithyandextask.service;

import dev.kolyapetrov.mypracticewithyandextask.entity.Import;

import java.util.HashMap;

public interface ImportService {
    HashMap<String, Long> saveImportData(Import importData);
}
