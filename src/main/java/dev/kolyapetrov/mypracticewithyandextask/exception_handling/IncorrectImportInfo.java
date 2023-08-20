package dev.kolyapetrov.mypracticewithyandextask.exception_handling;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class IncorrectImportInfo {
    private int statusCode;

    private List<String> info;
}
