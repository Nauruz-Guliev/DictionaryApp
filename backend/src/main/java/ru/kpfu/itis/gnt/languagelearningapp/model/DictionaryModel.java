package ru.kpfu.itis.gnt.languagelearningapp.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DictionaryModel {
    private List<DictionaryItem> dictionary;
    private List<String> imageUrls;
    private byte[] audio;
}
