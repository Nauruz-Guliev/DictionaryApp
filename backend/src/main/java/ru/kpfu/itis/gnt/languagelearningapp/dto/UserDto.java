package ru.kpfu.itis.gnt.languagelearningapp.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.gnt.languagelearningapp.entities.WordEntity;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {
    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String login;
    @NotEmpty
    private String token;
    private Set<DictionaryItem> words;
}
