package dev.kolyapetrov.mypracticewithyandextask.exception_handling;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IncorrectDataException extends RuntimeException {
    private List<String> listOfErrors;

    public IncorrectDataException(List<String> listOfErrors) {
        this.listOfErrors = listOfErrors;
    }
}
