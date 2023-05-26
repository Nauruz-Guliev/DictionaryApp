package ru.kpfu.itis.gnt.languagelearningapp.mappers.custom;

import org.springframework.stereotype.Component;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models.*;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;

import java.util.ArrayList;
import java.util.List;

@Component
public class DictionaryYandexApiResponseMapper implements BaseMapper<List<DictionaryItem>, YandexDictionaryResponse>{

    @Override
    public List<DictionaryItem> mapTo(YandexDictionaryResponse model) {
        ArrayList<DictionaryItem> list  = new ArrayList<>();
        for (DefItem item: model.getDef()) {
            List<String> meanings = new ArrayList<>();
            List<String> synonyms = new ArrayList<>();
            for (TrItem tr : item.getTr()) {
                if(tr.getSyn() != null) {
                    meanings.addAll(tr.getSyn().stream().map(SynItem::getText).toList());
                    meanings.add(tr.getText());
                }
                if(tr.getMean() != null) {
                    synonyms.addAll(tr.getMean().stream().map(MeanItem::getText).toList());
                }
            }
            list.add(
                    DictionaryItem.builder()
                            .meaning(meanings)
                            .synonyms(synonyms)
                            .originalWord(item.getText())
                            .type(item.getPos())
                            .build()
            );
        }
        return list;
    }

    @Override
    public YandexDictionaryResponse mapFrom(List<DictionaryItem> model) {
        return new YandexDictionaryResponse();
    }
}
