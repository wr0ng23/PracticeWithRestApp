package dev.kolyapetrov.mypracticewithyandextask.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.kolyapetrov.mypracticewithyandextask.validation.AtLeastOneNotNull;
import dev.kolyapetrov.mypracticewithyandextask.validation.group_of_validation.CitizenPatch;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AtLeastOneNotNull(groups = CitizenPatch.class) // For citizen patch
public class Citizen {

    @JsonProperty(value = "citizen_id", index = 1)
    @Positive(message = "The field citizen_id must be positive number")
    private Long citizen_id;

    @NotBlank(message = "The field town must contain at least 1 not blank char")
    @JsonProperty(index = 4)
    private String town;

    @NotBlank(message = "The field street must contain at least 1 not blank char")
    @JsonProperty(index = 5)
    private String street;

    @NotBlank(message = "The field building must contain at least 1 not blank char")
    @JsonProperty(index = 6)
    private String building;

    @Positive(message = "The field apartment must be positive number")
    @JsonProperty(index = 7)
    private Integer apartment;

    @NotBlank(message = "The field name must contain at least 1 not blank char")
    @JsonProperty(index = 2)
    private String name;

    @NotNull(message = "The field birth_date must be entered")
    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty(value = "birth_date", index = 3)
    @Past(message = "Entered date not your birth date")
    private LocalDate birthDate;

    @Pattern(regexp = "^(male|female)$", message = "The filed gender must be male or female")
    @JsonProperty(index = 8)
    private String gender;

    @NotNull(message = "The field relatives must be entered")
    @JsonProperty(index = 9)
    private List<Long> relatives;
}
