package ru.kpfu.itis.gnt.languagelearningapp.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.DictionaryApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models.YandexDictionaryResponse;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ErrorMessageConstants;
import ru.kpfu.itis.gnt.languagelearningapp.constants.QualifierConstants;
import ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.UserDto;
import ru.kpfu.itis.gnt.languagelearningapp.entities.*;
import ru.kpfu.itis.gnt.languagelearningapp.exception.DatabaseException;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.custom.DictionaryMapper;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.custom.DictionaryYandexApiResponseMapper;
import ru.kpfu.itis.gnt.languagelearningapp.model.DictionaryModel;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;
import ru.kpfu.itis.gnt.languagelearningapp.repository.*;

import java.io.IOException;
import java.util.*;

@Service
public class DictionaryService {
    private final Retrofit dictionaryRetrofit;
    private final DictionaryYandexApiResponseMapper dictionaryMapper;
    private final SynonymRepository synonymRepository;
    private final MeaningRepository meaningRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final DictionaryRepository dictionaryRepository;
    private final LocaleRepository localeRepository;
    private final WordTypeRepository wordTypeRepository;
    private final DictionaryMapper dictionaryLocalMapper;
    private final AudioService audioService;

    public DictionaryService(@Qualifier(QualifierConstants.DICTIONARY_QUALIFIER) Retrofit dictionaryRetrofit,
                             LocaleRepository localeRepository,
                             DictionaryRepository dictionaryRepository,
                             SynonymRepository synonymRepository,
                             MeaningRepository meaningRepository,
                             DictionaryYandexApiResponseMapper dictionaryMapper,
                             ImageService imageService,
                             UserRepository userRepository,
                             WordTypeRepository wordTypeRepository,
                             DictionaryMapper dictionaryLocalMapper,
                             AudioService audioService) {

        this.dictionaryRetrofit = dictionaryRetrofit;
        this.dictionaryRepository = dictionaryRepository;
        this.dictionaryMapper = dictionaryMapper;
        this.localeRepository = localeRepository;
        this.synonymRepository = synonymRepository;
        this.meaningRepository = meaningRepository;
        this.imageService = imageService;
        this.userRepository = userRepository;
        this.wordTypeRepository = wordTypeRepository;
        this.dictionaryLocalMapper = dictionaryLocalMapper;
        this.audioService = audioService;
    }

    public DictionaryModel getDictionaryModel(TranslationDto dto, UserDto userDto) {
        DictionaryModel model = new DictionaryModel();
        // получаем перевод. Если не получили - исключение
        model.setDictionary(getDictionaryItems(dto, userDto));
        // получаем аудио
        model.setAudio(audioService.loadAudioRemote(dto, dto.getText()));
        return model;
    }

    private List<DictionaryItem> getDictionaryItems(TranslationDto dto, UserDto userDto) {
        try {
            List<WordEntity> list = dictionaryRepository.findByTextIgnoreCaseAndLocale(dto.getText(), getLocale(dto.getFrom()));
            if (!list.isEmpty()) {
                return getDictionaryItemsLocal(dto, userDto, list);
            } else {
                return getDictionaryItemsRemote(dto, userDto);
            }
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.NOT_FOUND.DICTIONARY + " " + exception.getMessage());
        }
    }

    private List<DictionaryItem> getDictionaryItemsLocal(TranslationDto dto, UserDto userDto, List<WordEntity> list) {
        Set<DictionaryItem> items = new HashSet<>();
        for (WordEntity entity : list) {
            DictionaryItem item = DictionaryItem.builder()
                    .id(entity.getId())
                    //перевод
                    .originalWord(dto.getText())
                    //значения
                    .meaning(entity.getMeanings().stream().map(MeaningEntity::getText).toList())
                    // синонимы
                    .synonyms(entity.getSynonyms().stream().map(SynonymEntity::getText).toList())
                    // избранный
                    .isFavorite(checkIfFavorite(entity, userDto))
                    .build();
            items.add(item);
        }
        return new ArrayList<>(items);
    }

    private List<DictionaryItem> getDictionaryItemsRemote(TranslationDto dto, UserDto userDto) throws IOException {
        DictionaryApi dictionaryApi = dictionaryRetrofit.create(DictionaryApi.class);
        YandexDictionaryResponse responseTranslation = dictionaryApi.getTranslation(dto.getFrom().getLanguage() + "-" + dto.getTo().getLanguage(), dto.getText()).execute().body();
        //если не нашло, то выбрасываем исключение, которое ниже сразу отлавливаем
        if (responseTranslation == null) throw new RuntimeException();
        // если нашло, то кэшируем
        List<DictionaryItem> translationList = dictionaryMapper.mapTo(responseTranslation);
        for (DictionaryItem item : translationList) {
            //сначала создаем сущность общую
            WordEntity wordEntity;

            WordEntity wordEntityList = dictionaryRepository.findByTextAndType(
                    dto.getText(), item.getType());

            if (wordEntityList != null) {
                wordEntity = WordEntity.builder()
                        // text
                        .text(dto.getText())
                        //время сохранения
                        .dateAdded(Calendar.getInstance().getTimeInMillis())
                        // локаль
                        .build();
            } else {
                wordEntity = null;
            }

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
            WordTypeEntity wordTypeEntity = getWordType(item.getType());
            wordEntity.setType(wordTypeEntity);

            //ссылки на изображения
            List<ImageUrlEntity> imageList = imageService.getImageUrlEntityList(wordEntity, dto.getText(), dto.getFrom().getLanguage());
            wordEntity.setImages(imageList);
            try {
                dictionaryRepository.save(wordEntity);
            } catch (Exception ignored) {}
            item.setFavorite(checkIfFavorite(wordEntity, userDto));
            item.setId(wordEntity.getId());
        }
        return translationList;
    }

    private WordTypeEntity getWordType(String text) {
        try {
            return wordTypeRepository.findByTextCaseInsensitive(text).get(0);
        } catch (Exception exception) {
            return WordTypeEntity.builder().text(text).build();
        }
    }

    private boolean checkIfFavorite(WordEntity wordEntity, UserDto userDto) {
        return dictionaryRepository.isFavorite(userDto.getId(), wordEntity.getId());
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
                    SynonymEntity.builder().text(synonym).word(wordEntity).build())).toList();
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    private List<MeaningEntity> getMeanings(DictionaryItem item, WordEntity wordEntity) {
        try {
            return item.getMeaning().stream().map(meaning -> meaningRepository.findByText(meaning).orElse(
                    MeaningEntity.builder().text(meaning).word(wordEntity).build())).toList();
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }


    public DictionaryItem addFavorite(DictionaryItem item, UserDto userDto) {
        try {
            UserEntity userEntity = userRepository.findByLogin(userDto.getLogin()).get();
            WordEntity wordEntity = dictionaryRepository.findByTextAndType(item.getOriginalWord(), item.getType());
            userEntity.getWords().add(wordEntity);
            userRepository.save(userEntity);
            return dictionaryLocalMapper.mapTo(dictionaryRepository.findById(item.getId()).get(), true);
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.FAVORITE_ADD_FAIL + " due to " + exception.getMessage());
        }
    }


    public DictionaryItem deleteFavorite(DictionaryItem item, UserDto userDto) {
        try {
            UserEntity userEntity = userRepository.findByLogin(userDto.getLogin()).get();
            WordEntity wordEntity = dictionaryRepository.findByTextAndType(item.getOriginalWord(), item.getType());
            userEntity.getWords().remove(wordEntity);
            userRepository.save(userEntity);
            return dictionaryLocalMapper.mapTo(dictionaryRepository.findById(item.getId()).get(), false);
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.FAVORITE_REMOVE_FAIL + " due to " + exception.getMessage());
        }
    }

    public List<DictionaryItem> getUserWordEntityList(UserDto userDto) {
        try {
            List<WordEntity> wordEntities = dictionaryRepository.findByIds(userRepository.getFavoriteWordIds(userDto.getId()));
            List<DictionaryItem> list = new ArrayList<>();
            for (WordEntity wordEntity : wordEntities) {
                boolean isFavorite = dictionaryRepository.isFavorite(userDto.getId(), wordEntity.getId());
                list.add(dictionaryLocalMapper.mapTo(wordEntity, isFavorite));
            }
            return list;
        } catch (Exception exception) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.FAVORITES_FAIL);
        }
    }


    public Page<DictionaryItem> getUserWordEntityList(Pageable pageable, UserDto userDto) {
        try {
            Page<WordEntity> wordEntities = dictionaryRepository.findByIds(userRepository.getFavoriteWordIds(userDto.getId()), pageable);
            return wordEntities.map(wordEntity -> {
                boolean isFavorite = dictionaryRepository.isFavorite(userDto.getId(), wordEntity.getId());
                return dictionaryLocalMapper.mapTo(wordEntity, isFavorite);
            });
        } catch (Exception ex) {
            throw new DatabaseException(ErrorMessageConstants.INTERNAL.PAGING_FAILED);
        }
    }
}
