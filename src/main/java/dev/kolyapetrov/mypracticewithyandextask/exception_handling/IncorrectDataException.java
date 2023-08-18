package dev.kolyapetrov.mypracticewithyandextask.exception_handling;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class IncorrectDataException extends RuntimeException {
    private List<String> listOfErrors = new ArrayList<>();

    public IncorrectDataException(List<String> listOfErrors) {
        this.listOfErrors = listOfErrors;
    }

    public IncorrectDataException(String error) {
        listOfErrors.add(error);
    }
}
