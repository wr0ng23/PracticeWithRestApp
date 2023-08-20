package dev.kolyapetrov.mypracticewithyandextask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PercentilesByTown {
    @JsonProperty(index = 1)
    String town;

    @JsonProperty(index = 2)
    int p50;

    @JsonProperty(index = 3)
    int p75;

    @JsonProperty(index = 4)
    int p99;
}
