package dev.kolyapetrov.mypracticewithyandextask.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<IncorrectImportInfo> wrongDataCitizen(IncorrectDataException exception) {

        IncorrectImportInfo incorrectImportInfo = new IncorrectImportInfo();
        incorrectImportInfo.setInfo(exception.getListOfErrors());
        incorrectImportInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(incorrectImportInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectImportInfo> wrongDate(DateTimeParseException exception) {

        IncorrectImportInfo incorrectImportInfo = new IncorrectImportInfo();
        incorrectImportInfo.setInfo(List.of("Date must be in correct format DD-MM-YYYY"));
        incorrectImportInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(incorrectImportInfo, HttpStatus.BAD_REQUEST);
    }
}
