package ru.kpfu.itis.gnt.languagelearningapp.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.DictionaryApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models.YandexDictionaryResponse;
import ru.kpfu.itis.gnt.languagelearningapp.api.image.ImageApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.image.models.ImageResponse;
import ru.kpfu.itis.gnt.languagelearningapp.constants.QualifierConstants;
import ru.kpfu.itis.gnt.languagelearningapp.entities.ImageUrlEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.WordEntity;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.custom.ImageUrlListMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    private final Retrofit imageRetrofit;
    private final Retrofit dictionaryRetrofit;
    private final ImageUrlListMapper imageMapper;


    public ImageService(@Qualifier(QualifierConstants.IMAGE_QUALIFIER) Retrofit imageRetrofit,
                        @Qualifier(QualifierConstants.DICTIONARY_QUALIFIER) Retrofit dictionaryRetrofit,
                        ImageUrlListMapper imageMapper) {
        this.imageRetrofit = imageRetrofit;
        this.dictionaryRetrofit = dictionaryRetrofit;
        this.imageMapper = imageMapper;
    }


    public List<ImageUrlEntity> getImageUrlEntityList(WordEntity wordEntity, String text, String localeFrom) {
        List<ImageUrlEntity> list = new ArrayList<>();
        try {
            ImageApi imageApi = imageRetrofit.create(ImageApi.class);
            if (localeFrom.contains("en")) {
                ImageResponse response = imageApi.getImageResponse(text).execute().body();
                if (response != null) {
                    for (String item : imageMapper.mapTo(response)) {
                        try {
                            ImageUrlEntity imageUrlEntity = ImageUrlEntity.builder().url(item).word(wordEntity).build();
                            list.add(imageUrlEntity);
                        } catch (Exception ignored) {
                        }
                    }
                }
            } else {
                DictionaryApi dictionaryApi = dictionaryRetrofit.create(DictionaryApi.class);
                YandexDictionaryResponse response = dictionaryApi.getTranslation(localeFrom + "-" + "en", text).execute().body();
                String word = response.getDef().get(0).getTr().get(0).getText();
                ImageResponse image = imageApi.getImageResponse(word).execute().body();
                if (image == null) return list;
                for (String item : imageMapper.mapTo(image)) {
                    try {
                        ImageUrlEntity imageUrlEntity = ImageUrlEntity.builder().word(wordEntity).url(item).build();
                        list.add(imageUrlEntity);
                    } catch (Exception ignored) {
                    }
                }
            }
            return list;
        } catch (Exception exception) {
            return list;
        }
    }
}
