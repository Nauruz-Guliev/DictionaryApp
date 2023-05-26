package ru.kpfu.itis.gnt.languagelearningapp.mappers.custom;

import org.springframework.stereotype.Component;
import ru.kpfu.itis.gnt.languagelearningapp.api.image.models.ImageResponse;
import ru.kpfu.itis.gnt.languagelearningapp.api.image.models.ResultsItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ImageUrlListMapper implements BaseMapper<List<String>, ImageResponse>{

    @Override
    public List<String> mapTo(ImageResponse model) {
       Set<String> set = new HashSet<>();
       for (ResultsItem item: model.getResults()){
           set.add(item.getUrls().getRaw());
       }
       return new ArrayList<>(set);
    }

    @Override
    public ImageResponse mapFrom(List<String> model) {
        return new ImageResponse();
    }
}
