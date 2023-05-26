package ru.kpfu.itis.gnt.languagelearningapp.mappers.custom;

import org.springframework.stereotype.Component;
import ru.kpfu.itis.gnt.languagelearningapp.entities.MeaningEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.SynonymEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.WordEntity;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;

@Component
public class DictionaryMapper  {
    public DictionaryItem mapTo(WordEntity model, boolean isFavorite) {
        return DictionaryItem.builder().meaning(model.getMeanings().stream().map(
                        MeaningEntity::getText
                ).toList())
                .synonyms(
                        model.getSynonyms().stream().map(
                                SynonymEntity::getText
                        ).toList())
                .isFavorite(isFavorite)
                .originalWord(model.getText())
                .build();
    }

}
