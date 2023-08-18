package dev.kolyapetrov.mypracticewithyandextask.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.kolyapetrov.mypracticewithyandextask.validation.AtLeastOneNotNull;
import dev.kolyapetrov.mypracticewithyandextask.validation.group_of_validation.CitizenPatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

    @JsonProperty("citizen_id")
    @Positive(message = "The field citizen_id must be positive number")
    private Long citizen_id;

    @NotBlank(message = "The field town must contain at least 1 not blank char")
    private String town;

    @NotBlank(message = "The field street must contain at least 1 not blank char")
    private String street;

    @NotBlank(message = "The field building must contain at least 1 not blank char")
    private String building;

    @Positive(message = "The field apartment must be positive number")
    private Integer apartment;

    @NotBlank(message = "The field name must contain at least 1 not blank char")
    private String name;

    @NotNull(message = "The field birth_date must be entered")
    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @Pattern(regexp = "^(male|female)$", message = "The filed gender must be male or female")
    private String gender;

    @NotNull(message = "The field relatives must be entered")
    private List<Long> relatives;
}
