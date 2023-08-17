package dev.kolyapetrov.mypracticewithyandextask.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Citizen {

    @JsonProperty("citizen_id")
    @Positive(message = "The field citizen_id must be positive number")
    private Integer citizen_id;

    @NotBlank(message = "The field town must contain at least 1 not blank char")
    private String town;

    @NotBlank(message = "The field street must contain at least 1 not blank char")
    private String street;

    @NotBlank(message = "The field building must contain at least 1 not blank char")
    private String building;

    @Positive(message = "The field apartment must be positive number")
    private int apartment;

    @NotBlank(message = "The field name must contain at least 1 not blank char")
    private String name;

    @NotNull(message = "The field birth_date must be entered")
    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @Pattern(regexp = "^(male|female)$", message = "The filed gender must be male or female")
    private String gender;

    @NotNull(message = "The field relatives must be entered")
    private List<Integer> relatives;
}
