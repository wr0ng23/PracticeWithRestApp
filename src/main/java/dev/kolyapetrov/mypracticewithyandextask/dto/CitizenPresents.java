package dev.kolyapetrov.mypracticewithyandextask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CitizenPresents {
    @JsonProperty(value = "citizen_id", index = 1)
    long citizenId;

    long presents;
}
