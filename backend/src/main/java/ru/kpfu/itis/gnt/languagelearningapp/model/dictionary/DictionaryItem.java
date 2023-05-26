package ru.kpfu.itis.gnt.languagelearningapp.model.dictionary;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DictionaryItem {
    private String originalWord;
    private String type;
    private List<String> meaning;
    private List<String> synonyms;
    private Long id;
    private boolean isFavorite;
}
