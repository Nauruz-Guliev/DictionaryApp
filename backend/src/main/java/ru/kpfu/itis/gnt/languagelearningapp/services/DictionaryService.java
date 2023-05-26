package ru.kpfu.itis.gnt.languagelearningapp.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.DictionaryApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models.YandexDictionaryResponse;
import ru.kpfu.itis.gnt.languagelearningapp.api.image.ImageApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.image.models.ImageResponse;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ErrorMessageConstants;
import ru.kpfu.itis.gnt.languagelearningapp.constants.QualifierConstants;
import ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.UserDto;
import ru.kpfu.itis.gnt.languagelearningapp.entities.*;
import ru.kpfu.itis.gnt.languagelearningapp.exception.DatabaseException;
import ru.kpfu.itis.gnt.languagelearningapp.exception.ResourceNotFoundException;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.custom.DictionaryMapper;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.custom.DictionaryYandexApiResponseMapper;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.custom.ImageUrlListMapper;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.mapstruct.UserMapper;
import ru.kpfu.itis.gnt.languagelearningapp.model.DictionaryModel;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;
import ru.kpfu.itis.gnt.languagelearningapp.repository.*;

import java.util.*;

@Service
public class DictionaryService {
    private final Retrofit imageRetrofit;
    private final Retrofit speechRetrofit;
    private final Retrofit dictionaryRetrofit;
    private final DictionaryYandexApiResponseMapper dictionaryMapper;
    private final SynonymRepository synonymRepository;
    private final MeaningRepository meaningRepository;
    private final ImageUrlRepository imageUrlRepository;

    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final ImageUrlListMapper imageMapper;
    private final LocaleRepository localeRepository;
    private final DictionaryMapper dictionaryLocalMapper;
    private final UserMapper userMapper;

    public DictionaryService(@Qualifier(QualifierConstants.IMAGE_QUALIFIER) Retrofit imageRetrofit, @Qualifier(QualifierConstants.SPEECH_QUALIFIER) Retrofit speechRetrofit, @Qualifier(QualifierConstants.DICTIONARY_QUALIFIER) Retrofit dictionaryRetrofit,


                             LocaleRepository localeRepository, WordRepository wordRepository, SynonymRepository synonymRepository, MeaningRepository meaningRepository, ImageUrlListMapper imageMapper, DictionaryYandexApiResponseMapper dictionaryMapper, ImageUrlRepository imageUrlRepository, UserRepository userRepository, DictionaryMapper dictionaryLocalMapper, UserMapper userMapper) {

        this.imageRetrofit = imageRetrofit;
        this.speechRetrofit = speechRetrofit;
        this.dictionaryRetrofit = dictionaryRetrofit;
        this.wordRepository = wordRepository;
        this.imageMapper = imageMapper;
        this.dictionaryMapper = dictionaryMapper;
        this.localeRepository = localeRepository;
        this.synonymRepository = synonymRepository;
        this.meaningRepository = meaningRepository;
        this.imageUrlRepository = imageUrlRepository;
        this.userRepository = userRepository;
        this.dictionaryLocalMapper = dictionaryLocalMapper;
        this.userMapper = userMapper;
    }

    public DictionaryModel getDictionaryModel(TranslationDto dto, UserDto userDto) {
        DictionaryModel model = new DictionaryModel();
        // получаем перевод. Если не получили - исключение
        model.setDictionary(getDictionaryItems(dto, userDto));

        // получаем аудио
        /*
        TextToSpeechApi textToSpeechApi = speechRetrofit.create(TextToSpeechApi.class);
        try {
            if (model.getDictionary().size() > 0) {
                Response<ResponseBody> body = textToSpeechApi.getAudio(dto.getTo().toString().replace("_", "-"), dto.getText()).execute();
                System.out.println(body);
            }
        } catch (Exception ignored) {
        }

         */
        try {
            WordEntity wordEntity = wordRepository.findByTextCaseInsensitive(dto.getText()).get(0);
            List<String> list = imageUrlRepository.findAll().stream().filter(
                    imageUrlEntity -> Objects.equals(imageUrlEntity.getWord().getId(), wordEntity.getId())
            ).map(ImageUrlEntity::getUrl).toList();
            model.setImageUrls(list);
        } catch (Exception ignored) {

        }
        return model;
    }

    private List<DictionaryItem> getDictionaryItems(TranslationDto dto, UserDto userDto) {
        try {
            DictionaryApi dictionaryApi = dictionaryRetrofit.create(DictionaryApi.class);
            //если нашли локально
            List<WordEntity> list = wordRepository.findByTextAndLocale(dto.getText(), getLocale(dto.getTo()));
            if (!list.isEmpty()) {
                List<DictionaryItem> items = new ArrayList<>();
                for (WordEntity entity : list) {
                    DictionaryItem item = DictionaryItem.builder()
                            .id(entity.getId())
                            //перевод
                            .originalWord(dto.getText())
                            //значения
                            .meaning(entity.getMeanings().stream().map(MeaningEntity::getText).toList())
                            // синонимы
                            .synonyms(entity.getSynonyms().stream().map(SynonymEntity::getText).toList())
                           .build();
                    items.add(item);
                }
                return items;
            }
            //если не нашли локально
            else {
                YandexDictionaryResponse responseTranslation = dictionaryApi.getTranslation(dto.getFrom().getLanguage() + "-" + dto.getTo().getLanguage(), dto.getText()).execute().body();
                //если не нашло, то выбрасываем исключение, которое ниже сразу отлавливаем
                if (responseTranslation == null) throw new RuntimeException();

                // если нашло, то кэшируем
                List<DictionaryItem> translationList = dictionaryMapper.mapTo(responseTranslation);
                for (DictionaryItem item : translationList) {
                    YandexDictionaryResponse responseReverse = dictionaryApi.getTranslation(dto.getTo().getLanguage() + "-" + dto.getFrom().getLanguage(), dto.getText()).execute().body();
                    if (responseReverse == null) throw new RuntimeException();

                    //сначала создаем сущность общую

                    WordEntity wordEntity;

                    List<WordEntity> wordEntityList = wordRepository.findByTextAndLocale(
                            dto.getText(), getLocale(dto.getFrom()));
                    if (wordEntityList.isEmpty()) {
                        wordEntity = WordEntity.builder()
                                // text
                                .text(dto.getText())
                                //время сохранения
                                .dateAdded(Calendar.getInstance().getTimeInMillis())
                                // локаль
                                .build();
                    } else {
                        wordEntity = wordEntityList.get(0);
                    }

                    //затем добавляем добавочные поля

                    //синонимы
                    List<SynonymEntity> synonyms = getSynonyms(item, wordEntity);
                    wordEntity.setSynonyms(synonyms);

                    //значения
                    List<MeaningEntity> meanings = getMeanings(item, wordEntity);
                    wordEntity.setMeanings(meanings);

                    //локаль
                    LocaleEntity locale = getLocale(dto.getFrom());
                    wordEntity.setLocale(locale);

                    //тип слова

                    //ссылки на изображения
                    List<ImageUrlEntity> imageList = getImageUrlEntityList(wordEntity, dto.getText(), dto.getFrom().getLanguage(), dto.getTo().getLanguage());
                    wordEntity.setImages(imageList);
                    locale.setWords(list);

                    if (wordRepository.findByTextCaseInsensitive(dto.getText()).isEmpty()) {
                        wordEntity = wordRepository.save(wordEntity);
                    }

                    //     wordRepository.save(wordEntity);
                    item.setFavorite(checkIfFavorite(wordEntity, userDto));
                    item.setId(wordEntity.getId());
                }
                return translationList;
            }

        } catch (Exception exception) {
            throw new ResourceNotFoundException(ErrorMessageConstants.NOT_FOUND.DICTIONARY + " " + exception.getMessage());
        }
    }

    private boolean checkIfFavorite(WordEntity wordEntity, UserDto userDto) {
        return userRepository.isFavorite(userDto.getId(), wordEntity.getId());
    }




    private List<ImageUrlEntity> getImageUrlEntityList(WordEntity wordEntity, String text, String localeFrom, String localeTo) {
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

    private LocaleEntity getLocale(Locale locale) {
        return localeRepository.findByTextCaseInsensitive(locale.toString()).orElse(
                LocaleEntity.builder()
                        .text(locale.toString())
                        .build()
        );
    }

    private List<SynonymEntity> getSynonyms(DictionaryItem item, WordEntity wordEntity) {
        try {
            return item.getSynonyms().stream().map(synonym -> synonymRepository.findByText(synonym).orElse(
                    SynonymEntity.builder().word(wordEntity).text(synonym).build())).toList();
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    private List<MeaningEntity> getMeanings(DictionaryItem item, WordEntity wordEntity) {
        try {
            return item.getMeaning().stream().map(meaning -> meaningRepository.findByText(meaning).orElse(
                    MeaningEntity.builder().word(wordEntity).text(meaning).build())).toList();
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }


    public DictionaryItem addWordLike(Long wordEntityId, UserDto userDto) {

        try {
            userRepository.addWordToFavorite(userDto.getId(), wordEntityId);
            return dictionaryLocalMapper.mapTo(wordRepository.findById(wordEntityId).get(), true);
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.FAVORITE_ADD_FAIL + " due to " + exception.getMessage());
        }
    }


    public DictionaryItem deleteWordLike(Long wordEntityId, UserDto userDto) {
        try {
            userRepository.removeWordFromFavorite(userDto.getId(), wordEntityId);
            return dictionaryLocalMapper.mapTo(wordRepository.findById(wordEntityId).get(), false);
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.FAVORITE_REMOVE_FAIL + " due to " + exception.getMessage());
        }
    }

    public List<DictionaryItem> getUserWordEntityList(UserDto userDto) {
        try {
            List<WordEntity> wordEntities = wordRepository.findByIds(userRepository.getFavoriteWordIds(userDto.getId()));
            List<DictionaryItem> list = new ArrayList<>();
            for (WordEntity wordEntity : wordEntities) {
                boolean isFavorite = userRepository.isFavorite(userDto.getId(), wordEntity.getId());
                list.add(dictionaryLocalMapper.mapTo(wordEntity, isFavorite));
            }
            return list;
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.FAVORITES_FAIL);
        }
    }


    public Page<DictionaryItem> findAll(Pageable pageable, UserDto userDto) {
        try {
            Page<WordEntity> wordEntities = wordRepository.findByIds(userRepository.getFavoriteWordIds(userDto.getId()), pageable);
            return wordEntities.map(wordEntity -> {
                boolean isFavorite = userRepository.isFavorite(userDto.getId(), wordEntity.getId());
                return dictionaryLocalMapper.mapTo(wordEntity, isFavorite);
            });
        } catch (Exception ex) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.PAGING_FAILED);
        }
    }
}
