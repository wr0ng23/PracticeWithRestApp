package dev.kolyapetrov.mypracticewithyandextask.entity;

import dev.kolyapetrov.mypracticewithyandextask.validation.CheckRelatives;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "Imports")
public class Import {
    @Id
    private String id;

    @CheckRelatives
    List<Citizen> citizens;
}

